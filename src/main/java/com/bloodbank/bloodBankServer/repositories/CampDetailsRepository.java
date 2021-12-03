package com.bloodbank.bloodBankServer.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodBankServer.model.CampDetails;
import com.bloodbank.bloodBankServer.model.DonorHistory;

public interface CampDetailsRepository extends JpaRepository<CampDetails, Long> {

//    List<CampDetails> findByUserId(Long id);

    List<CampDetails> findByDate(Date date);
}
