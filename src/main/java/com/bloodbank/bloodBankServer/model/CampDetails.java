package com.bloodbank.bloodBankServer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class CampDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String campDetail;
    @Getter
    private Date date;
    @Getter
    private String campLocation;
    @Getter
    private String startTime;
    @Getter
    private String endTime;

//    @OneToMany(mappedBy = "campDetails", cascade = CascadeType.ALL)
//    private Set<UserCamp> userCampsSet;

//    public CampDetails(String campDetail, String campLocation, Date date, String startTime, String endTime, UserCamp... userCamps) {
    public CampDetails(String campDetail, String campLocation, Date date, String startTime, String endTime) {
        this.campDetail = campDetail;
        this.campLocation = campLocation;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
//        for (UserCamp userCamp : userCamps) {
//            userCamp.setCampDetails(this);
//        }
//        this.userCampsSet = Stream.of(userCamps).collect(Collectors.toSet());
    }


}
