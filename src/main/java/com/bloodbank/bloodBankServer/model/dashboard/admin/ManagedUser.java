package com.bloodbank.bloodBankServer.model.dashboard.admin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class ManagedUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private String empRegId;
    private String empRegHospital;

    public ManagedUser(String firstName, String lastName, String idNumber, String address, String phoneNumber, String email, String empRegId, String empRegHospital) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.empRegId = empRegId;
        this.empRegHospital = empRegHospital;
    }
}
