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
public class RequestedStocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Date date;
    private String productType;
    private int units;
    private String hospitalId;
    private String requestFromHospitalId;
    private String requestStatus;
    private String reason;

    public RequestedStocks(Date date, String productType, int units, String hospitalId, String requestFromHospitalId, String requestStatus, String reason) {
        this.date = date;
        this.productType = productType;
        this.units = units;
        this.hospitalId = hospitalId;
        this.requestFromHospitalId = requestFromHospitalId;
        this.requestStatus = requestStatus;
        this.reason = reason;
    }
}
