package com.bloodbank.bloodBankServer.model.dashboard.stocks;

import javax.persistence.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SearchBloodRequest {
    private String productType;
    private int quantity;
}
