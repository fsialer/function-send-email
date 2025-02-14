package com.fernando.func.send.email.services;

import com.fernando.func.send.email.client.FollowerRestClient;
import com.fernando.func.send.email.client.impl.FollowerRestClientImpl;
import com.fernando.func.send.email.dto.Follower;

import java.util.List;

public class FollowerService {
    private final FollowerRestClient followerRestClient;

    public FollowerService(){
        followerRestClient=new FollowerRestClientImpl();
    }

    public List<Follower> findFollowers(Long authorId,String token) {
        return followerRestClient.findFollowers(authorId,token);
    }
}
