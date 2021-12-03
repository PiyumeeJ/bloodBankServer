package com.bloodbank.bloodBankServer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String donorId;
    private String streetAddress;
    private String city;
    private String mobileNumber;
    private String bloodType;
    private String password;
    private String email;
    private String role;
    private String gender;
}
