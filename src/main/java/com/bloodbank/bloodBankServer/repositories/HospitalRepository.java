package com.bloodbank.bloodBankServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bloodbank.bloodBankServer.model.dashboard.hospitals.Hospitals;

public interface HospitalRepository extends CrudRepository<Hospitals, Long> {

    Hospitals findByHospitalId(String hospitalId);
}
