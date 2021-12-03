package com.bloodbank.bloodBankServer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bloodbank.bloodBankServer.model.CampDetails;
import com.bloodbank.bloodBankServer.model.CampDetailsDto;
import com.bloodbank.bloodBankServer.model.TestModelDto;
import com.bloodbank.bloodBankServer.model.UserCamp;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

//    @Query("SELECT NEW com.bloodbank.bloodBankServer.model.CampDetailsDto(c.id, c.campDetail, c.campLocation) FROM CampDetails AS c JOIN UserCamp AS u ON u.id=c.id where u.id=1")
//    @Query("SELECT NEW com.bloodbank.bloodBankServer.model.CampDetailsDto(c.id, c.campDetail, c.campLocation) FROM CampDetails AS c JOIN User AS u ON u.id=c.id where u.id = ?1")
//    List<CampDetails> getUserId(Long userId);

//    @Query("SELECT NEW com.bloodbank.bloodBankServer.model.TestModelDto(u.address) FROM User AS u")
//    List<TestModelDto> getUserId(Long userId);

//    @Query(value = "SELECT camp_details.id, camp_details.camp_detail, camp_details.camp_location FROM camp_details INNER JOIN user_camp ON user_camp.camp_details_id=camp_details.id where user_camp.user_id=1 group by id", nativeQuery = true)
//    @Query(value = "SELECT camp_details.id, camp_details.camp_detail, camp_details.camp_location FROM camp_details INNER JOIN user_camp ON user_camp.camp_details_id=camp_details.id where user_camp.user_id=1", nativeQuery = true)
//    List<Object[]> getObjectForUser(Long userId);

    List<UserCamp> findByUserId(Long userId);
}
