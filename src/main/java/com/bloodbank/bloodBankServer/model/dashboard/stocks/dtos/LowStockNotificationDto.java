package com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LowStockNotificationDto {
    private Long id;
    private String hospitalId;
    private String productType;
    private String currentStock;
    private boolean acknowledged;
}
