package com.bloodbank.bloodBankServer.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.HospitalStock;

public interface HospitalStockRepository extends CrudRepository<HospitalStock, Long> {

    HospitalStock findByDateAndHospitalId(Date date, String hospitalId);

    List<HospitalStock> findByHospitalId(String hospitalId);

    List<HospitalStock> findByDate(Date date);
}
