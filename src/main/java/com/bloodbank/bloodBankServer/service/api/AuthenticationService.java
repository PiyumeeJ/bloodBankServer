package com.bloodbank.bloodBankServer.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodBankServer.client.AuthenticationClient;
import com.bloodbank.bloodBankServer.model.Token;
import com.bloodbank.bloodBankServer.model.ValidatedToken;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationClient client;

    public ValidatedToken authenticateToken(String stringToken) {
        Token token = new Token();
        token.setToken(stringToken);
        return client.getValidatedToken(token);
    }

}
