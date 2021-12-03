package com.bloodbank.bloodBankServer.model.dashboard.hospitals;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class Hospitals implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
