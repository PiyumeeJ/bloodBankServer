package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewStocksDto {
    private String id;
    private String date;
    private String hospitalId;
    private String productType;
    private String units;
    private String donorId;
    private String campId;
    private String campName;
    private String extractedDate;
    private String expiredDate;
    private String unitId;
}
