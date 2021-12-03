package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestedStocksDto {

    private String id;
    private String date;
    private String productType;
    private int units;
    private String hospitalId;
    private String requestFromHospitalId;
    private String requestStatus;
    private String reason;
}
