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
public class NewStocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Date date;
    private String hospitalId;
    private String productType;
    private String units;
    private String donorId;
    private String campId;
    private Date extractedDate;
    private Date expiredDate;
    private String unitId;

    public NewStocks(Date date, String hospitalId, String productType, String units, String donorId, String campId, Date extractedDate, Date expiredDate, String unitId) {
        this.date = date;
        this.hospitalId = hospitalId;
        this.productType = productType;
        this.units = units;
        this.donorId = donorId;
        this.campId = campId;
        this.extractedDate = extractedDate;
        this.expiredDate = expiredDate;
        this.unitId = unitId;
    }
}
