package com.bloodbank.bloodBankServer.model.dashboard.hospitals;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospitalDto implements Serializable {
    private String hospitalName;
    private String hospitalId;
    private String telephoneNumber;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String province;
    private String hospitalLocationLat;
    private String hospitalLocationLong;
}
