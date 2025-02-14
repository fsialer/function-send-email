package com.fernando.func.send.email.config.keyvault;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

public class KeyVaultService {
    private KeyVaultService(){}
    private static final String KEY_VAULT_URI = System.getenv("KEY_VAULT_URI");

    public static String getSecret(String secretName) {
        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl(KEY_VAULT_URI)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
        return secretClient.getSecret(secretName).getValue();
    }
}
