package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveStockDto {

    private String id;
    private String date;
    private String productType;
    private String units;
    private String hospitalId;
    private String dispatchDate;
    private String reason;
    private String requestedHospitalId;
}
