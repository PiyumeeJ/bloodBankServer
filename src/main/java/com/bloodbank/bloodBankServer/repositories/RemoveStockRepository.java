package com.bloodbank.bloodBankServer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.RemoveStock;

public interface RemoveStockRepository extends CrudRepository<RemoveStock, Long> {

    List<RemoveStock> findByHospitalId(String hospitalId);
}
