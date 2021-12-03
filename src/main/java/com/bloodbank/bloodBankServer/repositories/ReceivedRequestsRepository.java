package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.ReceivedRequests;

public interface ReceivedRequestsRepository extends CrudRepository<ReceivedRequests, Long> {
}
