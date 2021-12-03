package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainStockDto {
    private String id;
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
