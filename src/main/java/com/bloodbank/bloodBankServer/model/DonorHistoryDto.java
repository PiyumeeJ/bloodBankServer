package com.bloodbank.bloodBankServer.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonorHistoryDto {
    private String campId;
    private String donorId;
}
