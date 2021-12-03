package com.bloodbank.bloodBankServer.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private String firstName;
    private String lastName;
    private String idNumber;
    private String donorId;
    private String streetAddress;
    private String city;
    private String mobileNumber;
    private String bloodType;
    private String email;
    private String gender;
}
