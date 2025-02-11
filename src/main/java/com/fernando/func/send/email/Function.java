package com.fernando.func.send.email;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.func.send.email.client.AuthorizationRestClient;
import com.fernando.func.send.email.client.FollowerRestClient;
import com.fernando.func.send.email.client.UserRestClient;
import com.fernando.func.send.email.client.impl.AuthorizationRestClientImpl;
import com.fernando.func.send.email.client.impl.FollowerRestClientImpl;
import com.fernando.func.send.email.client.impl.UserRestClientImpl;
import com.fernando.func.send.email.config.KeyVaultService;
import com.fernando.func.send.email.dto.*;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

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


    private AuthorizationRestClient authorizationRestClient;
    private UserRestClient userRestClient;
    private FollowerRestClient followerRestClient;

    @FunctionName("functionSendEmail")
    public void  run(
            @ServiceBusTopicTrigger(
                    name = "message",
                    topicName = "send-notifications",
                    subscriptionName = "send-notification-email-post",
                    connection = "AzureServiceBusConnection")
                String message,
            final ExecutionContext context) {
                authorizationRestClient = new AuthorizationRestClientImpl();
                userRestClient = new UserRestClientImpl();
                followerRestClient = new FollowerRestClientImpl();

        try {
            Notification notification = toNotification(message);


            Token token = obtainToken();
            switch (notification.getTargetType()){
                case "POST":
                    casePost(notification,token,context);
                    break;
                case "COMMENT":
                    caseComment(notification,token,context);
                    break;
                case "FOLLOWER":
                    caseFollower(notification,token,context);
                    break;
                default:
                    throw new RuntimeException("Type notification not exists: " + notification.getTargetType());
            }
        } catch (Exception e) {
            context.getLogger().severe("Error processing message: " + e.getMessage());
            throw new RuntimeException("Error processing message: " + e.getMessage(), e);
        }
    }

    private void caseFollower(Notification notification, Token token,final ExecutionContext context){

    }

    private void caseComment(Notification notification, Token token,final ExecutionContext context){

    }

    private void casePost(Notification notification, Token token,final ExecutionContext context) throws IOException, MessagingException {
        Author author = toAuthor(notification, token);
        List<Follower> followers = toFollowerList(notification, token);
        if(!followers.isEmpty()){
            String subject = "Twitter clone - Nuevo Post de ".concat(author.getNames());
            String content = "<h3>El usuario ".concat(author.getNames()).concat(" a realizado una nueva publicación.</h3><p><a href='#'>").concat(notification.getContent()).concat("</a></p>");
            for (Follower follower :followers){
                switch (System.getenv("FLAG_EMAIL")){
                    case "SEND_GRID":
                        Response response = sendEmail(follower.getEmail(),content,subject);
                        context.getLogger().info("Correo enviado a: " + follower.getEmail()+": "+response.getStatusCode());
                        break;
                    case "SMTP_EMAIL":
                        sendEmailSmtp(follower.getEmail(),content,subject);
                        context.getLogger().info("Correo enviado a: " + follower.getEmail()+".");
                        break;
                    default:
                        throw new RuntimeException("FLAG_EMAIL not found.");
                }
            }
        }else{
            context.getLogger().info("El usuario ".concat(author.getNames()).concat(" no tiene followers."));
        }
    }


    private void sendEmailSmtp(String toEmail,String content,String subject) throws MessagingException {
        final String username = KeyVaultService.getSecret("smtp-user");
        final String password = KeyVaultService.getSecret("smtp-password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", KeyVaultService.getSecret("smtp-host"));
        props.put("mail.smtp.port", KeyVaultService.getSecret("smtp-port"));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(KeyVaultService.getSecret("smtp-sender-email")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html");
            Transport.send(message);
    }

    private Response sendEmail(String toEmail,String content,String subject) throws IOException {
            Email from = new Email(KeyVaultService.getSecret("send-grid-sender-email"));
            Email to = new Email(toEmail);
            Content emailContent = new Content("text/html", content);
            Mail mail = new Mail(from, subject, to, emailContent);
            SendGrid sg = new SendGrid(KeyVaultService.getSecret("send-grid-api-key"));
            Request sendGridRequest = new Request();
            sendGridRequest.setMethod(Method.POST);
            sendGridRequest.setEndpoint("mail/send");
            sendGridRequest.setBody(mail.build());
            return sg.api(sendGridRequest);
    }

    private Notification toNotification(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, Notification.class);
    }

    private Token obtainToken() throws IOException {

        HttpResponse<String> response = authorizationRestClient.obtainToken(
                KeyVaultService.getSecret("client-id-client-credentials"),
                KeyVaultService.getSecret("client-secret-client-credentials"),
                "client_credentials",
                "read");
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to obtain token, status code: " + response.statusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Token.class);
    }

    private Author toAuthor(Notification notification, Token token) throws IOException {
        HttpResponse<String> response = userRestClient.findById(notification.getUserId(), token.getAccess_token());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch author, status code: " + response.statusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Author.class);
    }

    private List<Follower> toFollowerList(Notification notification, Token token) throws IOException {
        HttpResponse<String> response = followerRestClient.findFollowers(notification.getUserId(), token.getAccess_token());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch followers, status code: " + response.statusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Follower>>() {});
    }
}
