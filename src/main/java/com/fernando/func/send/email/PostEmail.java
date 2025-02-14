package com.fernando.func.send.email;

import com.fernando.func.send.email.config.facade.email.EmailFacade;

public class PostEmail {

    public Boolean sendEmail(String toEmail,String author,String content) {
        EmailFacade emailFacade=new EmailFacade();
        return emailFacade.sendEmail(toEmail,body(author,content),subject(author));
    }

    public String subject(String author){
        return "Twitter clone - New Post ["
                .concat(author)
                .concat("]");
    }

    public String body(String author,String content){
        return "<h3> "
                .concat(author)
                .concat(" do new post.</h3><p><a href='#'>")
                .concat(content)
                .concat("</a></p>");
    }
}
