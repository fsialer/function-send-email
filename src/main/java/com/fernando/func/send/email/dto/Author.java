package com.fernando.func.send.email.dto;

public class Author extends User {
    private Long id;
    private String username;
    private String names;
    private String email;

    

    public Author(Long id, String username, String names, String email) {
        this.id = id;
        this.username = username;
        this.names = names;
        this.email = email;
    }

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
