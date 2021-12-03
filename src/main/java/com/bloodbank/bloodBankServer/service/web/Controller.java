package com.bloodbank.bloodBankServer.service.web;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodBankServer.client.AuthenticationClient;
import com.bloodbank.bloodBankServer.model.CampDetails;
import com.bloodbank.bloodBankServer.model.CampDetailsDto;
import com.bloodbank.bloodBankServer.model.DonorHistory;
import com.bloodbank.bloodBankServer.model.DonorHistoryDto;
import com.bloodbank.bloodBankServer.model.LoginUser;
import com.bloodbank.bloodBankServer.model.Post;
import com.bloodbank.bloodBankServer.model.TestModelDto;
import com.bloodbank.bloodBankServer.model.User;
import com.bloodbank.bloodBankServer.model.UserCamp;
import com.bloodbank.bloodBankServer.model.UserDto;
import com.bloodbank.bloodBankServer.model.UserResponseDto;
import com.bloodbank.bloodBankServer.repositories.CampDetailsRepository;
import com.bloodbank.bloodBankServer.repositories.DonorHistoryRepository;
import com.bloodbank.bloodBankServer.repositories.LoginUserRepository;
import com.bloodbank.bloodBankServer.repositories.UserCampRepository;
import com.bloodbank.bloodBankServer.repositories.UserRepository;
import com.bloodbank.bloodBankServer.service.helper.HandleSms;

import lombok.SneakyThrows;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    LoginUserRepository loginUserRepository;

    @Autowired
    CampDetailsRepository campDetailsRepository;

    @Autowired
    UserCampRepository userCampDetails;

    @Autowired
    DonorHistoryRepository donorHistoryRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserDto userDto) {
        LoginUser loginUser = loginUserRepository.findByUserName(userDto.getIdNumber());
        if (loginUser != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getIdNumber(), userDto.getStreetAddress(), userDto.getCity(),
                userDto.getGender(), userDto.getEmail(), userDto.getMobileNumber(), userDto.getBloodType(), userDto.getDonorId());
        User savedUser = userRepository.save(user);
        loginUser = new LoginUser(userDto.getIdNumber(), userDto.getPassword(), userDto.getRole(), savedUser);
        loginUserRepository.save(loginUser);
        return ResponseEntity.ok("User Created");
    }


    @PostMapping("/user/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@RequestBody UserDto userDto, Principal principal) {
        User user = userRepository.getById(Long.valueOf(principal.getName()));
        Optional.ofNullable(userDto.getFirstName()).ifPresent(s -> user.setFirstName(userDto.getFirstName()));
        Optional.ofNullable(userDto.getLastName()).ifPresent(s -> user.setLastName(userDto.getLastName()));
        Optional.ofNullable(userDto.getMobileNumber()).ifPresent(s -> user.setPhoneNumber(userDto.getMobileNumber()));
        Optional.ofNullable(userDto.getEmail()).ifPresent(s -> user.setEmail(userDto.getEmail()));
        Optional.ofNullable(userDto.getStreetAddress()).ifPresent(s -> user.setStreetAddress(userDto.getStreetAddress()));
        Optional.ofNullable(userDto.getCity()).ifPresent(s -> user.setCity(userDto.getCity()));
        userRepository.save(user);
        if (userDto.getPassword() != "") {
            Optional.ofNullable(userDto.getPassword()).ifPresent(s -> {
                LoginUser loginUser = loginUserRepository.getById(user.getId());
                loginUser.setPassword(userDto.getPassword());
                loginUserRepository.save(loginUser);
            });
        }
        return "Successful";
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> requestUserDetails(Principal principal) {
        User user = userRepository.getById(Long.valueOf(principal.getName()));
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setBloodType(user.getBloodType());
        responseDto.setEmail(user.getEmail());
        responseDto.setGender(user.getGender());
        responseDto.setIdNumber(user.getIdNumber());
        responseDto.setDonorId(user.getDonorId());
        responseDto.setMobileNumber(user.getPhoneNumber());
        responseDto.setStreetAddress(user.getStreetAddress());
        responseDto.setCity(user.getCity());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/userDetails")
    public ResponseEntity getUserDetails(Principal principal) {
        User user = userRepository.getById(Long.valueOf(principal.getName()));
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/donor/camps")
    public List<CampDetails> getCampDetailsForUser(Principal principal) {
        User user = userRepository.getById(Long.valueOf(principal.getName()));
        List<DonorHistory> donorHistories = donorHistoryRepository.findByDonorId(String.valueOf(user.getId()));
        List<CampDetails> campDetailsList = new ArrayList<>();
        for(DonorHistory donorHistory : donorHistories) {
            campDetailsList.add(campDetailsRepository.findById(Long.valueOf(donorHistory.getCampId())).get());
        }
        return campDetailsList;
    }

    @GetMapping("/donor/history")
    public List<CampDetails> getDonorHistory(Principal principal) {
        User user = userRepository.getById(Long.valueOf(principal.getName()));
        List<DonorHistory> histories = donorHistoryRepository.findByDonorId(String.valueOf(user.getId()));
        List<CampDetails> campDetailsList = new ArrayList<>();
        for(DonorHistory donorHistory : histories) {
            campDetailsList.add(campDetailsRepository.getById(Long.valueOf(donorHistory.getCampId())));
        }
        return campDetailsList;
    }

    @GetMapping("/donor/check")
    @SneakyThrows
    public List<DonorHistory> getDonorByDate() {
        System.out.println("Hello");
        return  null;
    }

    @GetMapping("/donor/{id}")
    public UserDto getUser(@PathVariable String id) {
        User user = userRepository.getById(Long.valueOf(id));
        UserDto userDto = new UserDto();
        userDto.setId(user.getId().toString());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setIdNumber(user.getIdNumber());
        userDto.setDonorId(user.getDonorId());
        userDto.setStreetAddress(user.getStreetAddress());
        userDto.setCity(user.getCity());
        userDto.setMobileNumber(user.getPhoneNumber());
        userDto.setBloodType(user.getBloodType());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        LoginUser loginUser = loginUserRepository.getById(user.getId());
        userDto.setPassword(loginUser.getPassword());
        userDto.setRole(loginUser.getRole());
        return userDto;
    }

    @PostMapping("/user/update/all")
    @ResponseStatus(HttpStatus.OK)
    public String updateEntireUser(@RequestBody UserDto userDto) {
        User user = userRepository.getById(Long.valueOf(userDto.getId()));
        Optional.ofNullable(userDto.getFirstName()).ifPresent(s -> user.setFirstName(userDto.getFirstName()));
        Optional.ofNullable(userDto.getLastName()).ifPresent(s -> user.setLastName(userDto.getLastName()));
        Optional.ofNullable(userDto.getIdNumber()).ifPresent(s -> user.setIdNumber(userDto.getIdNumber()));
        Optional.ofNullable(userDto.getDonorId()).ifPresent(s -> user.setDonorId(userDto.getDonorId()));
        Optional.ofNullable(userDto.getStreetAddress()).ifPresent(s -> user.setStreetAddress(userDto.getStreetAddress()));
        Optional.ofNullable(userDto.getCity()).ifPresent(s -> user.setCity(userDto.getCity()));
        Optional.ofNullable(userDto.getMobileNumber()).ifPresent(s -> user.setPhoneNumber(userDto.getMobileNumber()));
        Optional.ofNullable(userDto.getEmail()).ifPresent(s -> user.setEmail(userDto.getEmail()));
        Optional.ofNullable(userDto.getGender()).ifPresent(s -> user.setGender(userDto.getGender()));
        userRepository.save(user);
        if (userDto.getPassword() != "") {
            Optional.ofNullable(userDto.getPassword()).ifPresent(s -> {
                LoginUser loginUser = loginUserRepository.getById(user.getId());
                loginUser.setUserName(userDto.getIdNumber());
                loginUser.setPassword(userDto.getPassword());
                loginUserRepository.save(loginUser);
            });
        }
        return "Successful";
    }
}
