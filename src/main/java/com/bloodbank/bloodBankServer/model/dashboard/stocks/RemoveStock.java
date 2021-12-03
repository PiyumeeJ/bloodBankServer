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
public class RemoveStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Date date;
    private String productType;
    private String units;
    private String hospitalId;
    private Date dispatchDate;
    private String reason;
    private String requestedHospitalId;

    public RemoveStock(Date date, String productType, String units, String hospitalId, Date dispatchDate, String reason, String requestedHospitalId) {
        this.date = date;
        this.productType = productType;
        this.units = units;
        this.hospitalId = hospitalId;
        this.dispatchDate = dispatchDate;
        this.reason = reason;
        this.requestedHospitalId = requestedHospitalId;
    }
}
