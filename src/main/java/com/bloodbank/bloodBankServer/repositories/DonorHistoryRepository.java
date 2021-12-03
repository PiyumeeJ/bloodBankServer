package com.bloodbank.bloodBankServer.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.DonorHistory;

public interface DonorHistoryRepository extends CrudRepository<DonorHistory, Long> {

    List<DonorHistory> findByDonorId(String id);

    List<DonorHistory> findByCampId(String id);
}
