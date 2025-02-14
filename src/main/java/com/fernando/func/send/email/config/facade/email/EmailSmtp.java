package com.fernando.func.send.email.config.facade.email;

import com.fernando.func.send.email.config.keyvault.KeyVaultService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class EmailSmtp implements Email {

    @Override
    public Boolean sendEmail(String toEmail, String content, String subject) {
        try{
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", KeyVaultService.getSecret("smtp-host"));
            props.put("mail.smtp.port", KeyVaultService.getSecret("smtp-port"));

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(KeyVaultService.getSecret("smtp-user"),
                            KeyVaultService.getSecret("smtp-password"));
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(KeyVaultService.getSecret("smtp-sender-email")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Error sending email: " + e.getMessage(), e);
            return  false;
        }
    }
}
