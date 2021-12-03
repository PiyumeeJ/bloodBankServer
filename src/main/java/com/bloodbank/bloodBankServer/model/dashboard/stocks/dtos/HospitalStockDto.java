package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospitalStockDto {
    private String id;
    private String date;
    private String hospitalId;
    private String oNegative;
    private String oPlus;
    private String aNegative;
    private String aPlus;
    private String bNegative;
    private String bPlus;
    private String abNegative;
    private String abPlus;
    private String plasma;
    private String platelets;
    private String cryoprecipitate;
    private String ffp;
}
