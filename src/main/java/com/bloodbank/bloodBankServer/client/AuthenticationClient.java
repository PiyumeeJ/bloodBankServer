package com.bloodbank.bloodBankServer.client;


import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bloodbank.bloodBankServer.model.Token;
import com.bloodbank.bloodBankServer.model.ValidatedToken;

@FeignClient(value = "jplaceholder", url = "http://localhost:8083")
public interface AuthenticationClient {

    @PostMapping("/validate")
    ValidatedToken getValidatedToken(@RequestBody Token token);

}
