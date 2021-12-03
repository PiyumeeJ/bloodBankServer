package com.bloodbank.bloodBankServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampDetailsDto {
    private String date;
    private String details;
    private String start;
    private String end;
    private String location;
//    private String userId;
}
