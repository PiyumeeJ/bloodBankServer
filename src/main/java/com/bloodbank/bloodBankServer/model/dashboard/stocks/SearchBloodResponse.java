package com.bloodbank.bloodBankServer.model.dashboard.stocks;

import lombok.Data;

@Data
public class SearchBloodResponse {
    private String hospitalId;
    private String hospitalName;
    private String phoneNumber;
    private String address;
    private int distance;
}
