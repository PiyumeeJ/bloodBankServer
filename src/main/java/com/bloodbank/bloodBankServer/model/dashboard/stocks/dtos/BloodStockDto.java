package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BloodStockDto {
    private Long id;
    private Date date;
    private int oNegative;
    private int oPlus;
    private int aNegative;
    private int aPlus;
    private int bNegative;
    private int bPlus;
    private int abNegative;
    private int abPlus;
    private int plasma;
    private int platelets;
    private int cryoprecipitate;
    private int ffp;
}
