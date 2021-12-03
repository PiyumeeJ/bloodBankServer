package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceivedRequestsDto {

    private String id;
    private String date;
    private String requestId;
    private String productType;
    private String units;
    private String hospitalId;
    private String requestCameFrom;
    private String requestStatus;
    private String reason;
}
