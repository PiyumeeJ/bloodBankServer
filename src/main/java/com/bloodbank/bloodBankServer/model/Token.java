package com.bloodbank.bloodBankServer.model;

import java.io.Serializable;

public class Token implements Serializable {

    private String token;

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
