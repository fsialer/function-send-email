package com.fernando.func.send.email.client;

import com.fernando.func.send.email.dto.Follower;

import java.util.List;

public interface FollowerRestClient {
    List<Follower> findFollowers(Long authorId, String token);
}
