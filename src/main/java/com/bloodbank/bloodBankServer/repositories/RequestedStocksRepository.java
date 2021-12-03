package com.bloodbank.bloodBankServer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.RequestedStocks;

public interface RequestedStocksRepository extends CrudRepository<RequestedStocks, Long> {

    List<RequestedStocks> findByRequestFromHospitalId(String hospitalId);

    List<RequestedStocks> findByHospitalId(String hospitalId);

    List<RequestedStocks> findByHospitalIdAndRequestStatus(String hospitalId, String requestStatus);
}
