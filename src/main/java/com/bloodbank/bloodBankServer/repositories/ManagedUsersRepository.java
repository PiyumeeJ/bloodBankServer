package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.admin.ManagedUser;

public interface ManagedUsersRepository extends CrudRepository<ManagedUser, Long> {
}
