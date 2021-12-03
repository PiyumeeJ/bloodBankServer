package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodBankServer.model.LoginUser;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

    LoginUser findByUserName(String userName);
}
