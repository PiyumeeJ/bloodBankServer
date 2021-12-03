package com.bloodbank.bloodBankServer.model.dashboard.admin;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUsersDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private String empRegId;
    private String empRegHospital;
    private String password;
    private String role;
    private String activeStatus;
}
