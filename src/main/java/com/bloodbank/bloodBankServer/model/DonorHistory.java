package com.bloodbank.bloodBankServer.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class DonorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
//    @Getter
//    private String location;
//    @Getter
//    private Date date;

    private String campId;

    private String donorId;

    public DonorHistory(String campId, String donorId) {
        this.campId = campId;
        this.donorId = donorId;
    }

//    public User checkInitializedUser() {
//        return this.user;
//    }
}
