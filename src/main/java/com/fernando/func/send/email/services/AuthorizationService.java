package com.fernando.func.send.email.services;

import com.fernando.func.send.email.client.AuthorizationRestClient;
import com.fernando.func.send.email.client.impl.AuthorizationRestClientImpl;
import com.fernando.func.send.email.config.keyvault.KeyVaultService;
import com.fernando.func.send.email.dto.Token;

public class AuthorizationService {
    private final AuthorizationRestClient authorizationRestClient;

    public AuthorizationService(){
        authorizationRestClient= new AuthorizationRestClientImpl();
    }

    public Token obtainToken(){
        return authorizationRestClient.obtainToken(
                KeyVaultService.getSecret("client-id-client-credentials"),
                KeyVaultService.getSecret("client-secret-client-credentials"),
                "client_credentials",
                "read");
    }
}
