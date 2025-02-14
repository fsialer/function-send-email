package com.fernando.func.send.email.services;

import com.fernando.func.send.email.client.UserRestClient;
import com.fernando.func.send.email.client.impl.UserRestClientImpl;
import com.fernando.func.send.email.dto.User;

public class UserService {
    private final UserRestClient userRestClient;

    public UserService(){
        userRestClient=new UserRestClientImpl();
    }

    public User findUser(Long userId, String token) {
        return userRestClient.findById(userId, token);
    }
}
