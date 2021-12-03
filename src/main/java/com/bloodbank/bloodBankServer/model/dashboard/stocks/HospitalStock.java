package com.bloodbank.bloodBankServer.model.dashboard.stocks;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class HospitalStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Date date;
    private String hospitalId;
    private int oNegative;
    private int oPlus;
    private int aNegative;
    private int aPlus;
    private int bNegative;
    private int bPlus;
    private int abNegative;
    private int abPlus;
    private int plasma;
    private int platelets;
    private int cryoprecipitate;
    private int ffp;

    public HospitalStock(Date date, String hospitalId, int oNegative, int oPlus, int aNegative, int aPlus, int bNegative, int bPlus, int abNegative, int abPlus, int plasma, int platelets, int cryoprecipitate, int ffp) {
        this.date = date;
        this.hospitalId = hospitalId;
        this.oNegative = oNegative;
        this.oPlus = oPlus;
        this.aNegative = aNegative;
        this.aPlus = aPlus;
        this.bNegative = bNegative;
        this.bPlus = bPlus;
        this.abNegative = abNegative;
        this.abPlus = abPlus;
        this.plasma = plasma;
        this.platelets = platelets;
        this.cryoprecipitate = cryoprecipitate;
        this.ffp = ffp;
    }
}
