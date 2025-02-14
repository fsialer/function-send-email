package com.fernando.func.send.email.config.facade.email;

public interface Email {
    Boolean sendEmail(String toEmail, String content,String subject);
}
