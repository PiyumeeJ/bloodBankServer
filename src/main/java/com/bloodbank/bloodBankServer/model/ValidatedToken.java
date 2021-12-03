package com.bloodbank.bloodBankServer.model;


import java.io.Serializable;

public class ValidatedToken implements Serializable {

    private String userId;
    private String role;
    private String validationStatus;

    public ValidatedToken() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
}
