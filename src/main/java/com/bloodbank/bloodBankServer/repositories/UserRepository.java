package com.bloodbank.bloodBankServer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodBankServer.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByBloodType(String bloodType);
}
