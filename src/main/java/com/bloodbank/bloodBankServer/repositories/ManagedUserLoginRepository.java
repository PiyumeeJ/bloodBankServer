package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.admin.ManagedUserLogin;

public interface ManagedUserLoginRepository extends CrudRepository<ManagedUserLogin, Long> {

}
