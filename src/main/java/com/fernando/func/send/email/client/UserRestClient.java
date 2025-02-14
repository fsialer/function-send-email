package com.fernando.func.send.email.client;

import com.fernando.func.send.email.dto.User;

public interface UserRestClient {
    User findById(Long userId, String token) ;
}
