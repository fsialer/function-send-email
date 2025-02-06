package com.fernando.func.send.email.dto;

public class Token {
    private String access_token;
    private String scope;
    private String token_type;
    private Integer expires_in;


    
    public Token() {
    }

    public Token(String access_token, String scope, String token_type, Integer expires_in) {
        this.access_token = access_token;
        this.scope = scope;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
