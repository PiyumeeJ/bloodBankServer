package com.bloodbank.bloodBankServer.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String idNumber;
    private String donorId;
    private String streetAddress;
    private String city;
    private String phoneNumber;
    private String gender;
    private String email;
    private String bloodType;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    @PrimaryKeyJoinColumn
//    private LoginUser loginUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserCamp> userCamps = new HashSet<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<DonorHistory> donorHistories;

//    public User(String firstName, String lastName,String idNumber, String address, String phoneNumber, String bloodType, String activeAreas, LoginUser loginUser) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.idNumber = idNumber;
//        this.address = address;
//        this.phoneNumber = phoneNumber;
//        this.bloodType = bloodType;
//        this.activeAreas = activeAreas;
//        this.loginUser = loginUser;
//        this.loginUser.setUser(this);
//    }

    public User(String firstName, String lastName,String idNumber, String streetAddress, String city, String gender, String email, String phoneNumber, String bloodType, String donorId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.donorId = donorId;
        this.streetAddress = streetAddress;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.bloodType = bloodType;
        this.gender = gender;
        this.email = email;
    }
}
