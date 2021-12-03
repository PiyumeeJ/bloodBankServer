package com.bloodbank.bloodBankServer.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.NewStocks;

public interface NewStocksRepository extends CrudRepository<NewStocks, Long> {

    NewStocks findByDateAndHospitalId(Date date, String hospitalId);

    List<NewStocks> findByHospitalId(String hospitalId);
}
