package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.stocks.LowStockNotification;

public interface LowStockNotificationRepository extends CrudRepository<LowStockNotification, Long> {
}
