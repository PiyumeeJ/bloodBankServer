package com.bloodbank.bloodBankServer.repositories;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.MainStock;

public interface MainStockRepository extends CrudRepository<MainStock, Long> {

    MainStock findByDate(Date date);

}
