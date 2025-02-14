package com.fernando.func.send.email.config.facade.email;

import com.fernando.func.send.email.config.keyvault.KeyVaultService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class SendGrid implements Email {

    @Override
    public Boolean sendEmail(String toEmail, String content, String subject) {
        try{
            com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(KeyVaultService.getSecret("send-grid-sender-email"));
            com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(toEmail);
            Content emailContent = new Content("text/html", content);
            Mail mail = new Mail(from, subject, to, emailContent);
            com.sendgrid.SendGrid sg = new com.sendgrid.SendGrid(KeyVaultService.getSecret("send-grid-api-key"));
            Request sendGridRequest = new Request();
            sendGridRequest.setMethod(Method.POST);
            sendGridRequest.setEndpoint("mail/send");
            sendGridRequest.setBody(mail.build());
            Response response=sg.api(sendGridRequest);
            return response.getStatusCode()==204;
        } catch (Exception e) {
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Error sending email: " + e.getMessage(), e);
            return  false;
        }
    }
}
