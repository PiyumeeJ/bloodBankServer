package com.bloodbank.bloodBankServer.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bloodbank.bloodBankServer.model.CampDetails;
import com.bloodbank.bloodBankServer.model.DonorHistory;
import com.bloodbank.bloodBankServer.model.User;
import com.bloodbank.bloodBankServer.repositories.CampDetailsRepository;
import com.bloodbank.bloodBankServer.repositories.DonorHistoryRepository;
import com.bloodbank.bloodBankServer.repositories.UserRepository;
import com.bloodbank.bloodBankServer.service.helper.HandleSms;

import lombok.SneakyThrows;

@Component
public class SmsScheduler {

    @Autowired
    DonorHistoryRepository donorHistoryRepository;

    @Autowired
    CampDetailsRepository campDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    HandleSms handleSms;


//    @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 6 * * *")
    public void sendSms() {
        System.out.println("scheler started");
        sendSmsForFourMonthsOldDonors(4);
        sendSmsForMoreThanFourMonthsOldDonors(8);
        sendSmsForMoreThanFourMonthsOldDonors(12);
    }

    public void sendSmsForFourMonthsOldDonors(int months) {
        months = -months;
        Date date = neutralDate(new Date());
        Date newDate = DateUtils.addMonths(date, months);
//        List<DonorHistory> donors = donorHistoryRepository.findByDate(newDate);
        List<CampDetails> campDetails = campDetailsRepository.findByDate(newDate);
        List<DonorHistory> donorHistories = new ArrayList<>();
        for(CampDetails campDetails1 : campDetails) {
            donorHistories.addAll(donorHistoryRepository.findByCampId(String.valueOf(campDetails1.getId())));
        }
        String message = "HI %s. Now you are almost ready to save another life.";
        for (DonorHistory history : donorHistories) {
            User user = userRepository.getById(Long.valueOf(history.getDonorId()));
            String formatText = String.format(message, user.getFirstName());
            handleSms.sendSmsMessage(user.getPhoneNumber(), formatText);
        }
    }

    @SneakyThrows
    public void sendSmsForMoreThanFourMonthsOldDonors(int months) {
        months = -months;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(new Date());
        Date date = sdf.parse(sdf.format(new Date()));

        Date newDate = DateUtils.addMonths(date, months);
//        List<DonorHistory> donorHistories = donorHistoryRepository.findByDate(newDate);
        List<CampDetails> campDetails = campDetailsRepository.findByDate(newDate);
        List<DonorHistory> donorHistories = new ArrayList<>();
        for(CampDetails campDetails1 : campDetails) {
            donorHistories.addAll(donorHistoryRepository.findByCampId(String.valueOf(campDetails1.getId())));
        }
        for(DonorHistory donorHistory : donorHistories) {
            User user = userRepository.getById(Long.valueOf(donorHistory.getDonorId()));
            Long userId = user.getId();
            List<DonorHistory> histories = donorHistoryRepository.findByDonorId(String.valueOf(userId));
            List<Date> dateList = new ArrayList<>();
            for (DonorHistory history : histories) {
                CampDetails campDetails1 =  campDetailsRepository.getById(Long.valueOf(history.getCampId()));
                dateList.add(campDetails1.getDate());
            }
            dateList.sort(Comparator.naturalOrder());
            Collections.sort(dateList, Collections.reverseOrder());
            String message = "HI {}. Today you have passed {} months after you last blood donation. Login to website or reach to the nearest blood bank for your next donation";
            if (dateList.get(0).compareTo(newDate) == 0) {
                months = +months;
                String formatText = String.format(message, user.getFirstName(), months);
                handleSms.sendSmsMessage(user.getPhoneNumber(), formatText);
            }
        }

    }

    public Date neutralDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
