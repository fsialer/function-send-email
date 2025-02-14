package com.fernando.func.send.email.config.facade.email;

import com.fernando.func.send.email.exceptions.EmailFacadeException;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class EmailFacade {
    public Boolean sendEmail(String toEmail,String content,String subject) {
        try{
            switch (System.getenv("FLAG_EMAIL")){
                case "SEND_GRID"-> {
                    SendGrid sendGrid=new SendGrid();
                    return sendGrid.sendEmail( toEmail, content,subject);
                }
                case "EMAIL_SMTP"->{
                    EmailSmtp emailSmtp =new EmailSmtp();
                    return emailSmtp.sendEmail( toEmail, content,subject);
                }
                default -> throw new EmailFacadeException("Drive email no existing.");
            }
        }catch (EmailFacadeException e){
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Error sending email: " + e.getMessage()+"!", e);
            return false;
        }
    }
}
