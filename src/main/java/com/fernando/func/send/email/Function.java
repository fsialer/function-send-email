package com.fernando.func.send.email;

import com.azure.core.util.logging.LogLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.func.send.email.config.keyvault.KeyVaultService;
import com.fernando.func.send.email.dto.*;
import com.fernando.func.send.email.exceptions.EmailSendingException;
import com.fernando.func.send.email.services.AuthorizationService;
import com.fernando.func.send.email.services.FollowerService;
import com.fernando.func.send.email.services.UserService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;
import java.io.IOException;
import java.util.List;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    // Crear un cliente HTTP

    @FunctionName("functionSendEmail")
    public void  run(
            @ServiceBusTopicTrigger(
                    name = "message",
                    topicName = "send-notifications",
                    subscriptionName = "send-notification-email-post",
                    connection = "AzureServiceBusConnection")
                String message,
            final ExecutionContext context) {

        try {
            Notification notification = toNotification(message);
            AuthorizationService authorizationService=new AuthorizationService();
            Token token =authorizationService.obtainToken();
            if (notification.getTargetType().equals("POST")) {
                casePost(notification, token);
            }
            System.out.println("Envioado");
        } catch (Exception ex) {
            context.getLogger().severe("Error processing message: " + ex.getMessage());
        }
    }

    private void casePost(Notification notification, Token token) {
        try{
            UserService userService=new UserService();
            User author = userService.findUser(notification.getUserId(),token.getAccess_token());
            FollowerService followerService= new FollowerService();
            List<Follower> followers = followerService.findFollowers(author.getId(),token.getAccess_token());
            if(!followers.isEmpty()){
                for (Follower follower :followers){
                    PostEmail postEmail=new PostEmail();
                    if(!postEmail.sendEmail(follower.getEmail(), author.getNames(), notification.getContent())){
                        throw new EmailSendingException("Don't send email.");
                    }
                }
            }else{
                LOGGER.log(LogLevel.INFORMATIONAL, () -> "User ".concat(author.getUsername())
                        .concat(" haven't followers."));
            }
        } catch (EmailSendingException e) {
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Error sending email: " + e.getMessage(), e);
        }
    }

    private Notification toNotification(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, Notification.class);
    }
}
