package com.bloodbank.bloodBankServer.service.web;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.util.SloppyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodBankServer.client.AuthenticationClient;
import com.bloodbank.bloodBankServer.model.CampDetails;
import com.bloodbank.bloodBankServer.model.CampDetailsDto;
import com.bloodbank.bloodBankServer.model.DonorHistory;
import com.bloodbank.bloodBankServer.model.DonorHistoryDto;
import com.bloodbank.bloodBankServer.model.LoginUser;
import com.bloodbank.bloodBankServer.model.User;
import com.bloodbank.bloodBankServer.model.UserDto;
import com.bloodbank.bloodBankServer.model.UserResponseDto;
import com.bloodbank.bloodBankServer.model.dashboard.admin.ManageUsersDto;
import com.bloodbank.bloodBankServer.model.dashboard.admin.ManagedUserLogin;
import com.bloodbank.bloodBankServer.model.dashboard.admin.ManagedUser;
import com.bloodbank.bloodBankServer.model.dashboard.hospitals.HospitalDto;
import com.bloodbank.bloodBankServer.model.dashboard.hospitals.Hospitals;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.HospitalStock;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.LowStockNotification;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.MainStock;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.NewStocks;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.RemoveStock;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.RequestedStocks;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.SearchBloodRequest;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.SearchBloodResponse;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.BloodStockDto;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.BloodStockInStringDateDto;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.LowStockNotificationDto;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.NewStocksDto;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.RemoveStockDto;
import com.bloodbank.bloodBankServer.model.dashboard.stocks.dtos.RequestedStocksDto;
import com.bloodbank.bloodBankServer.repositories.CampDetailsRepository;
import com.bloodbank.bloodBankServer.repositories.DonorHistoryRepository;
import com.bloodbank.bloodBankServer.repositories.HospitalRepository;
import com.bloodbank.bloodBankServer.repositories.HospitalStockRepository;
import com.bloodbank.bloodBankServer.repositories.LoginUserRepository;
import com.bloodbank.bloodBankServer.repositories.MainStockRepository;
import com.bloodbank.bloodBankServer.repositories.ManagedUserLoginRepository;
import com.bloodbank.bloodBankServer.repositories.ManagedUsersRepository;
import com.bloodbank.bloodBankServer.repositories.NewStocksRepository;
import com.bloodbank.bloodBankServer.repositories.RemoveStockRepository;
import com.bloodbank.bloodBankServer.repositories.RequestedStocksRepository;
import com.bloodbank.bloodBankServer.repositories.UserCampRepository;
import com.bloodbank.bloodBankServer.repositories.UserRepository;

import lombok.SneakyThrows;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin")
public class AdminController {

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

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    ManagedUserLoginRepository managedUserLoginRepository;

    @Autowired
    ManagedUsersRepository managedUsersRepository;

    ////////////////////////////
    @Autowired
    NewStocksRepository newStocksRepository;

    @Autowired
    HospitalStockRepository hospitalStockRepository;

    @Autowired
    MainStockRepository mainStockRepository;

    @Autowired
    RemoveStockRepository removeStockRepository;

    @Autowired
    RequestedStocksRepository requestedStocksRepository;

    @PostMapping("/donor/camps")
    @SneakyThrows
    public void setCampDetails(@RequestBody CampDetailsDto campDto) {
//        User user1 = userRepository.getById(Long.valueOf(campDto.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = campDto.getDate();
        Date date = sdf.parse(strDate);
        CampDetails campDetails = new CampDetails(campDto.getDetails(), campDto.getLocation(), date, campDto.getStart(),
                campDto.getEnd());
        campDetailsRepository.save(campDetails);
        System.out.println("Call is Done");
    }

    @PostMapping("/donor/history")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public void addDonorHistory(@RequestBody DonorHistoryDto donorHistory) {
        DonorHistory dHistory = new DonorHistory(donorHistory.getCampId(), donorHistory.getDonorId());
        donorHistoryRepository.save(dHistory);
    }

    @GetMapping("/camps/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public List<CampDetails> getAllCamps() {
        return campDetailsRepository.findAll();
    }

    @PostMapping("/stocks/request")
    public void makeProductRequest(@RequestBody RequestedStocksDto requestedStocks, Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());

        RequestedStocks stocks = new RequestedStocks();
        stocks.setDate(new Date());
        stocks.setHospitalId(requestedStocks.getHospitalId());
        stocks.setProductType(getProductToString(requestedStocks.getProductType()));
        stocks.setUnits(requestedStocks.getUnits());
        stocks.setRequestStatus("Pending");
        stocks.setRequestFromHospitalId(hospitals.getHospitalId());
        stocks.setReason(" ");

        requestedStocksRepository.save(stocks);
    }

    @GetMapping("/stocks/outgoing/requests")
    public List<RequestedStocks> getOutgoingRequests(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());

        return requestedStocksRepository.findByRequestFromHospitalId(hospitals.getHospitalId());
    }

    @GetMapping("/stocks/incoming/requests")
    public List<RequestedStocks> getIncommingRequests(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());

        return requestedStocksRepository.findByHospitalId(hospitals.getHospitalId());
    }

    @GetMapping("/stocks/incoming/requests/pending")
    public List<RequestedStocks> getIncommingRequestsPending(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());

        return requestedStocksRepository.findByHospitalIdAndRequestStatus(hospitals.getHospitalId(), "Pending");
    }

    @GetMapping("/stocks/all")
    public List<NewStocks> getNewStocksForTheHospital(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        return newStocksRepository.findByHospitalId(user.getEmpRegHospital());
    }

    @GetMapping("/stocks/removed/all")
    public List<RemoveStock> getRemovedStocksForTheHospital(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        return removeStockRepository.findByHospitalId(user.getEmpRegHospital());
    }


    @GetMapping("/users")
    public List<ManageUsersDto> retrieveManagedUsers() {
        List<ManageUsersDto> manageUsersDtosList = new ArrayList<>();
        List<ManagedUser> userList = new ArrayList<>();
        Iterable<ManagedUser> managedUsers = managedUsersRepository.findAll();
        managedUsers.forEach(user -> userList.add(user));

        userList.forEach(user -> {
            ManagedUserLogin userLogin = managedUserLoginRepository.findById(user.getId()).get();
            ManageUsersDto manageUsersDto = new ManageUsersDto();
            manageUsersDto.setId(user.getId());
            manageUsersDto.setFirstName(user.getFirstName());
            manageUsersDto.setLastName(user.getLastName());
            manageUsersDto.setEmail(user.getEmail());
            manageUsersDto.setIdNumber(user.getIdNumber());
            manageUsersDto.setAddress(user.getAddress());
            manageUsersDto.setPhoneNumber(user.getPhoneNumber());
            manageUsersDto.setEmpRegId(user.getEmpRegId());
            manageUsersDto.setEmpRegHospital(user.getEmpRegHospital());
            manageUsersDto.setRole(userLogin.getRole());
            manageUsersDto.setPassword(userLogin.getPassword());
            manageUsersDto.setActiveStatus(userLogin.getActiveStatus());
            manageUsersDtosList.add(manageUsersDto);
        });
        return manageUsersDtosList;
    }


    @GetMapping("/hospitals/all")
    public List<Hospitals> retrieveAllHospitals() {
        List<Hospitals> hospitalsList = new ArrayList<>();
        Iterable<Hospitals> hospitalsIterable = hospitalRepository.findAll();
        hospitalsIterable.forEach(o -> hospitalsList.add(o));
        return hospitalsList;
    }

    @GetMapping("/donors/all")
    public List<UserDto> allDonors() {
        List<UserDto> userList = new ArrayList<>();
        Iterable<User> userIterable = userRepository.findAll();
        userIterable.forEach(user -> {
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
            userList.add(userDto);
        });
        return userList;
    }



    //////////////////////////////////////////////////////////////////////////////////


    @GetMapping("/managed/user/{id}")
    public ManageUsersDto retrieveUser(@PathVariable("id") String userId) {
        ManagedUser managedUser = managedUsersRepository.findById(Long.valueOf(userId)).get();
        ManagedUserLogin userLogin = managedUserLoginRepository.findById(Long.valueOf(userId)).get();

        ManageUsersDto manageUsersDto = new ManageUsersDto();
        manageUsersDto.setId(managedUser.getId());
        manageUsersDto.setFirstName(managedUser.getFirstName());
        manageUsersDto.setLastName(managedUser.getLastName());
        manageUsersDto.setIdNumber(managedUser.getIdNumber());
        manageUsersDto.setAddress(managedUser.getAddress());
        manageUsersDto.setPhoneNumber(managedUser.getPhoneNumber());
        manageUsersDto.setEmail(managedUser.getEmail());
        manageUsersDto.setEmpRegId(managedUser.getEmpRegId());
        manageUsersDto.setEmpRegHospital(managedUser.getEmpRegHospital());
        manageUsersDto.setRole(userLogin.getRole());
        manageUsersDto.setPassword(userLogin.getPassword());
        manageUsersDto.setActiveStatus(userLogin.getActiveStatus());

        return manageUsersDto;
    }



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void registerManagedUsers(@RequestBody ManageUsersDto usersDto) {
        ManagedUser managedUsers = new ManagedUser(usersDto.getFirstName(), usersDto.getLastName(), usersDto.getIdNumber(), usersDto.getAddress(),
                usersDto.getPhoneNumber(), usersDto.getEmail(), usersDto.getEmpRegId(), usersDto.getEmpRegHospital());
        managedUsersRepository.save(managedUsers);

        ManagedUserLogin managedUserLogin = new ManagedUserLogin(usersDto.getIdNumber(), usersDto.getPassword(), usersDto.getRole(), usersDto.getActiveStatus(), managedUsers);
        managedUserLoginRepository.save(managedUserLogin);
    }


    @PostMapping("/user/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateUserForAdmin(@RequestBody ManageUsersDto usersDto) {
        ManagedUser managedUsers = managedUsersRepository.findById(usersDto.getId()).get();
        Optional.ofNullable(usersDto.getFirstName()).ifPresent(s -> managedUsers.setFirstName(usersDto.getFirstName()));
        Optional.ofNullable(usersDto.getLastName()).ifPresent(s -> managedUsers.setLastName(usersDto.getLastName()));
        Optional.ofNullable(usersDto.getIdNumber()).ifPresent(s -> managedUsers.setIdNumber(usersDto.getIdNumber()));
        Optional.ofNullable(usersDto.getAddress()).ifPresent(s -> managedUsers.setAddress(usersDto.getAddress()));
        Optional.ofNullable(usersDto.getPhoneNumber()).ifPresent(s -> managedUsers.setPhoneNumber(usersDto.getPhoneNumber()));
        Optional.ofNullable(usersDto.getEmail()).ifPresent(s -> managedUsers.setEmail(usersDto.getEmail()));
        Optional.ofNullable(usersDto.getEmpRegId()).ifPresent(s -> managedUsers.setEmpRegId(usersDto.getEmpRegId()));
        Optional.ofNullable(usersDto.getEmpRegHospital()).ifPresent(s -> managedUsers.setEmpRegHospital(usersDto.getEmpRegHospital()));
        managedUsersRepository.save(managedUsers);

        ManagedUserLogin managedUserLogin = managedUserLoginRepository.findById(usersDto.getId()).get();

        Optional.ofNullable(usersDto.getRole()).ifPresent(s -> managedUserLogin.setRole(usersDto.getRole()));
        Optional.ofNullable(usersDto.getIdNumber()).ifPresent(s -> managedUserLogin.setUserName(usersDto.getIdNumber()));
        Optional.ofNullable(usersDto.getActiveStatus()).ifPresent(s -> managedUserLogin.setActiveStatus(usersDto.getActiveStatus()));
        Optional.ofNullable(usersDto.getPassword()).ifPresent(s -> managedUserLogin.setPassword(usersDto.getPassword()));

        managedUserLoginRepository.save(managedUserLogin);

        return "Successful";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/hospitals/save")
    public void saveHospital(@RequestBody HospitalDto hospitalDto) {
        Hospitals hospitals = new Hospitals();
        hospitals.setHospitalName(hospitalDto.getHospitalName());
        hospitals.setHospitalId(hospitalDto.getHospitalId());
        hospitals.setTelephoneNumber(hospitalDto.getTelephoneNumber());
        hospitals.setStreetAddress(hospitalDto.getStreetAddress());
        hospitals.setCity(hospitalDto.getCity());
        hospitals.setPostalCode(hospitalDto.getPostalCode());
        hospitals.setProvince(hospitalDto.getProvince());
        hospitals.setHospitalLocationLat(hospitalDto.getHospitalLocationLat());
        hospitals.setHospitalLocationLong(hospitalDto.getHospitalLocationLong());
        hospitalRepository.save(hospitals);
    }

    //////////////////////////////////// new changes
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/stocks/add")
    public void addNewStocks(@RequestBody NewStocksDto newStocks, Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());
        if (hospitals == null) {
            newStocks.setHospitalId("Admin"+user.getEmpRegId());
        } else {
            newStocks.setHospitalId(hospitals.getHospitalId());
        }

        NewStocks stocks = new NewStocks(new Date(), newStocks.getHospitalId(),
                newStocks.getProductType(), newStocks.getUnits(), newStocks.getDonorId(), newStocks.getCampId(),
                getDateFromString(newStocks.getExtractedDate()), getDateFromString(newStocks.getExpiredDate()), newStocks.getUnitId());
        newStocksRepository.save(stocks);
        String productType = newStocks.getProductType();
        Date date = neutralDate(neutralDate(new Date()));
        String hospitalId = newStocks.getHospitalId();
        Integer units = Integer.valueOf(newStocks.getUnits());

        HospitalStock hospitalStock = hospitalStockRepository.findByDateAndHospitalId(date, hospitalId);
        if (hospitalStock == null) {
            hospitalStock = createNewStockForTheDay(date, hospitalId, productType, units);
        } else {
            hospitalStock = updateStock(hospitalStock, productType, units);
        }
        hospitalStockRepository.save(hospitalStock);

        MainStock mainStock = mainStockRepository.findByDate(date);
        if (mainStock == null) {
            mainStock = createMainStockForTheDay(date, productType, units);
        } else {
            mainStock = updateMainStock(mainStock, productType, units);
        }
        mainStockRepository.save(mainStock);
    }

    @PostMapping("/stocks/reject")
    public void rejectRequest(@RequestBody RemoveStockDto stockDto) {
        RequestedStocks requestedStocks = requestedStocksRepository.findById(Long.valueOf(stockDto.getId())).get();
        requestedStocks.setRequestStatus("Rejected");
        requestedStocks.setReason(stockDto.getReason());
        requestedStocksRepository.save(requestedStocks);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/stocks/remove")
    public void removeStocks(@RequestBody RemoveStockDto stockDto, Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());

        if (stockDto.getId() != null) {
            RequestedStocks requestedStocks = requestedStocksRepository.findById(Long.valueOf(stockDto.getId())).get();
            stockDto.setRequestedHospitalId(requestedStocks.getRequestFromHospitalId());
            stockDto.setUnits(Integer.toString(requestedStocks.getUnits()));
            stockDto.setProductType(requestedStocks.getProductType());

            requestedStocks.setRequestStatus("Released");
            requestedStocksRepository.save(requestedStocks);
            String shortProduct = getCorrectProductToShort(requestedStocks.getProductType());
            stockDto.setUnits(String.valueOf(requestedStocks.getUnits()));
            stockDto.setProductType(shortProduct);
        }
        RemoveStock removeStock = new RemoveStock(new Date(), stockDto.getProductType(), stockDto.getUnits(),
                hospitals.getHospitalId(), new Date(), stockDto.getReason(),
                stockDto.getRequestedHospitalId() == null ? hospitals.getHospitalId() : stockDto.getRequestedHospitalId());
        removeStockRepository.save(removeStock);
        String productType = stockDto.getProductType();
        Date date = neutralDate(new Date());
        String hospitalId = hospitals.getHospitalId();
        Integer units = Integer.valueOf(stockDto.getUnits());

        HospitalStock hospitalStock = hospitalStockRepository.findByDateAndHospitalId(date, hospitalId);
        if (hospitalStock == null) {
            hospitalStock = createNewStockForTheDayAndReduce(date, hospitalId, productType, units);
        } else {
            hospitalStock = reduceStock(hospitalStock, productType, units);
        }
        hospitalStockRepository.save(hospitalStock);

        MainStock mainStock = mainStockRepository.findByDate(date);
        if (mainStock == null) {
            mainStock = createMainStockForTheDayAndReduce(date, productType, units);
        } else {
            mainStock = reduceMainStock(mainStock, productType, units);
        }
        mainStockRepository.save(mainStock);
    }


    @GetMapping("/registered/hospital")
    public String getRegisteredHospitalForUser(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());
        return hospitals.getHospitalName();
    }

    @GetMapping("/hospital/stock/current")
    public BloodStockDto getCurrentHospitalBloodStock(Principal principal) {
        Date date = neutralDate(new Date());
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        HospitalStock hospitalStock = hospitalStockRepository.findByDateAndHospitalId(date, user.getEmpRegHospital());
        BloodStockDto bloodStockDto = new BloodStockDto();
        bloodStockDto.setId(hospitalStock.getId());
        bloodStockDto.setDate(hospitalStock.getDate());
        bloodStockDto.setONegative(hospitalStock.getONegative());
        bloodStockDto.setOPlus(hospitalStock.getOPlus());
        bloodStockDto.setANegative(hospitalStock.getANegative());
        bloodStockDto.setAPlus(hospitalStock.getAPlus());
        bloodStockDto.setBNegative(hospitalStock.getBNegative());
        bloodStockDto.setBPlus(hospitalStock.getBPlus());
        bloodStockDto.setAbNegative(hospitalStock.getAbNegative());
        bloodStockDto.setAbPlus(hospitalStock.getAbPlus());
        bloodStockDto.setPlasma(hospitalStock.getPlasma());
        bloodStockDto.setPlatelets(hospitalStock.getPlatelets());
        bloodStockDto.setCryoprecipitate(hospitalStock.getCryoprecipitate());
        bloodStockDto.setFfp(hospitalStock.getFfp());
        return bloodStockDto;
    }

    @GetMapping("/hospital/stock/all")
    public List<BloodStockInStringDateDto> getAllHospitalBloodStock(Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        List<HospitalStock> hospitalStockList =  hospitalStockRepository.findByHospitalId(user.getEmpRegHospital());
        List<BloodStockInStringDateDto> bloodStocks = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (HospitalStock stock: hospitalStockList) {
            BloodStockInStringDateDto bloodStockDto = new BloodStockInStringDateDto();
            bloodStockDto.setId(stock.getId());
            bloodStockDto.setDate(formatter.format(stock.getDate()));
            bloodStockDto.setONegative(stock.getONegative());
            bloodStockDto.setOPlus(stock.getOPlus());
            bloodStockDto.setANegative(stock.getANegative());
            bloodStockDto.setAPlus(stock.getAPlus());
            bloodStockDto.setBNegative(stock.getBNegative());
            bloodStockDto.setBPlus(stock.getBPlus());
            bloodStockDto.setAbNegative(stock.getAbNegative());
            bloodStockDto.setAbPlus(stock.getAbPlus());
            bloodStockDto.setPlasma(stock.getPlasma());
            bloodStockDto.setPlatelets(stock.getPlatelets());
            bloodStockDto.setCryoprecipitate(stock.getCryoprecipitate());
            bloodStockDto.setFfp(stock.getFfp());
            bloodStocks.add(bloodStockDto);
        }
        return bloodStocks;
    }

    @GetMapping("/main/stock/all")
    public List<BloodStockInStringDateDto> getAllMainBloodStock() {
        List<BloodStockInStringDateDto> mainStockList = new ArrayList<>();
        Iterable<MainStock> iterable = mainStockRepository.findAll();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        iterable.forEach(mainStock -> {
            BloodStockInStringDateDto bloodStockDto = new BloodStockInStringDateDto();
            bloodStockDto.setId(mainStock.getId());
            bloodStockDto.setDate(formatter.format(mainStock.getDate()));
            bloodStockDto.setONegative(mainStock.getONegative());
            bloodStockDto.setOPlus(mainStock.getOPlus());
            bloodStockDto.setANegative(mainStock.getANegative());
            bloodStockDto.setAPlus(mainStock.getAPlus());
            bloodStockDto.setBNegative(mainStock.getBNegative());
            bloodStockDto.setBPlus(mainStock.getBPlus());
            bloodStockDto.setAbNegative(mainStock.getAbNegative());
            bloodStockDto.setAbPlus(mainStock.getAbPlus());
            bloodStockDto.setPlasma(mainStock.getPlasma());
            bloodStockDto.setPlatelets(mainStock.getPlatelets());
            bloodStockDto.setCryoprecipitate(mainStock.getCryoprecipitate());
            bloodStockDto.setFfp(mainStock.getFfp());
            mainStockList.add(bloodStockDto);
        });
        return mainStockList;
    }

    @GetMapping("/main/stock/current")
    public BloodStockDto getCurrentMainBloodStock() {
        Date date = neutralDate(new Date());
        MainStock mainStock = mainStockRepository.findByDate(date);
        BloodStockDto bloodStockDto = new BloodStockDto();
        bloodStockDto.setId(mainStock.getId());
        bloodStockDto.setDate(mainStock.getDate());
        bloodStockDto.setONegative(mainStock.getONegative());
        bloodStockDto.setOPlus(mainStock.getOPlus());
        bloodStockDto.setANegative(mainStock.getANegative());
        bloodStockDto.setAPlus(mainStock.getAPlus());
        bloodStockDto.setBNegative(mainStock.getBNegative());
        bloodStockDto.setBPlus(mainStock.getBPlus());
        bloodStockDto.setAbNegative(mainStock.getAbNegative());
        bloodStockDto.setAbPlus(mainStock.getAbPlus());
        bloodStockDto.setPlasma(mainStock.getPlasma());
        bloodStockDto.setPlatelets(mainStock.getPlatelets());
        bloodStockDto.setCryoprecipitate(mainStock.getCryoprecipitate());
        bloodStockDto.setFfp(mainStock.getFfp());
        return bloodStockDto;
    }


    @PostMapping("/stock/search")
    public SearchBloodResponse searchBloodStocks(@RequestBody SearchBloodRequest request, Principal principal) {
        ManagedUser user = managedUsersRepository.findById(Long.valueOf(principal.getName())).get();
        Hospitals hospitals = hospitalRepository.findByHospitalId(user.getEmpRegHospital());
        Iterable<Hospitals> allHospitals =  hospitalRepository.findAll();
        List<Hospitals> hospitalsList = new ArrayList<>();
        allHospitals.forEach(hospitalsList::add);
        double minValue = 10000000;
        int requestedAmount = request.getQuantity();
        String requestedProduct = request.getProductType();
        Hospitals minHospital = hospitals;
        Date toDate = neutralDate(new Date());

        List<HospitalStock> currentStock = hospitalStockRepository.findByDate(toDate);
        List<String> availableHospitals = new ArrayList<>();

        for (HospitalStock stock: currentStock) {
            int val = getValueOfProduct(stock, requestedProduct);
            if (val > requestedAmount && !stock.getHospitalId().equals(hospitals.getHospitalId())) {
                availableHospitals.add(stock.getHospitalId());
            }
        }

        for(String hospitalId : availableHospitals) {
            Hospitals hospital = hospitalRepository.findByHospitalId(hospitalId);

            double minLat = Double.valueOf(minHospital.getHospitalLocationLat());
            double minLong = Double.valueOf(minHospital.getHospitalLocationLong());

            double newLat = Double.valueOf(hospital.getHospitalLocationLat());
            double newLong = Double.valueOf(hospital.getHospitalLocationLong());

            double distance = SloppyMath.haversinMeters(minLat, minLong, newLat, newLong);

            if(distance < minValue) {
                minValue = distance;
                minHospital = hospital;
            }
        }
        if(availableHospitals.isEmpty()) {
            return null;
        }
        SearchBloodResponse searchBloodResponse = new SearchBloodResponse();
        searchBloodResponse.setHospitalId(minHospital.getHospitalId());
        searchBloodResponse.setDistance((int)minValue);
        searchBloodResponse.setHospitalName(minHospital.getHospitalName());
        searchBloodResponse.setAddress(minHospital.getStreetAddress()+ ","+minHospital.getCity());
        searchBloodResponse.setPhoneNumber(minHospital.getTelephoneNumber());
        return searchBloodResponse;
    }

    @SneakyThrows
    public Date getDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        return sdf.parse(date);
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

    public HospitalStock createNewStockForTheDay(Date date, String hospitalId, String productType, Integer units) {
        Date previousDate = DateUtils.addDays(date, -1);
        HospitalStock oldHospitalStock = hospitalStockRepository.findByDateAndHospitalId(previousDate, hospitalId);
        HospitalStock hospitalStock = new HospitalStock();
        hospitalStock.setONegative(oldHospitalStock == null ? 0 : oldHospitalStock.getONegative());
        hospitalStock.setOPlus(oldHospitalStock == null ? 0 : oldHospitalStock.getOPlus());
        hospitalStock.setANegative(oldHospitalStock == null ? 0 : oldHospitalStock.getANegative());
        hospitalStock.setAPlus(oldHospitalStock == null ? 0 : oldHospitalStock.getAPlus());
        hospitalStock.setBNegative(oldHospitalStock == null ? 0 : oldHospitalStock.getBNegative());
        hospitalStock.setBPlus(oldHospitalStock == null ? 0 : oldHospitalStock.getBPlus());
        hospitalStock.setAbNegative(oldHospitalStock == null ? 0 : oldHospitalStock.getAbNegative());
        hospitalStock.setAbPlus(oldHospitalStock == null ? 0 : oldHospitalStock.getAbPlus());
        hospitalStock.setPlasma(oldHospitalStock == null ? 0 : oldHospitalStock.getPlasma());
        hospitalStock.setPlatelets(oldHospitalStock == null ? 0 : oldHospitalStock.getPlatelets());
        hospitalStock.setCryoprecipitate(oldHospitalStock == null ? 0 : oldHospitalStock.getCryoprecipitate());
        hospitalStock.setFfp(oldHospitalStock == null ? 0 : oldHospitalStock.getFfp());
        hospitalStock.setDate(neutralDate(date));
        hospitalStock.setHospitalId(hospitalId);
        switch(productType)
        {
            case "oNeg":
                hospitalStock.setONegative(hospitalStock.getONegative() + units);
                break;
            case "oPls":
                hospitalStock.setOPlus(hospitalStock.getOPlus() + units);
                break;
            case "aNeg":
                hospitalStock.setANegative(hospitalStock.getANegative() + units);
                break;
            case "aPls":
                hospitalStock.setAPlus(hospitalStock.getAPlus() + units);
                break;
            case "bNeg":
                hospitalStock.setBNegative(hospitalStock.getBNegative() + units);
                break;
            case "bPls":
                hospitalStock.setBPlus(hospitalStock.getBPlus() + units);
                break;
            case "abNeg":
                hospitalStock.setAbNegative(hospitalStock.getAbNegative() + units);
                break;
            case "abPls":
                hospitalStock.setAbPlus(hospitalStock.getAbPlus() + units);
                break;
            case "plasma":
                hospitalStock.setPlasma(hospitalStock.getPlasma() + units);
                break;
            case "platelets":
                hospitalStock.setPlatelets(hospitalStock.getPlatelets() + units);
                break;
            case "cryop":
                hospitalStock.setCryoprecipitate(hospitalStock.getCryoprecipitate() + units);
                break;
            case "ffp":
                hospitalStock.setFfp(hospitalStock.getFfp() + units);
                break;
            default:
        }
        return hospitalStock;
    }

    public HospitalStock updateStock(HospitalStock hospitalStock, String productType, int units) {
        switch(productType)
        {
            case "oNeg":
                hospitalStock.setONegative(hospitalStock.getONegative() + units);
                break;
            case "oPls":
                hospitalStock.setOPlus(hospitalStock.getOPlus() + units);
                break;
            case "aNeg":
                hospitalStock.setANegative(hospitalStock.getANegative() + units);
                break;
            case "aPls":
                hospitalStock.setAPlus(hospitalStock.getAPlus() + units);
                break;
            case "bNeg":
                hospitalStock.setBNegative(hospitalStock.getBNegative() + units);
                break;
            case "bPls":
                hospitalStock.setBPlus(hospitalStock.getBPlus() + units);
                break;
            case "abNeg":
                hospitalStock.setAbNegative(hospitalStock.getAbNegative() + units);
                break;
            case "abPls":
                hospitalStock.setAbPlus(hospitalStock.getAbPlus() + units);
                break;
            case "plasma":
                hospitalStock.setPlasma(hospitalStock.getPlasma() + units);
                break;
            case "platelets":
                hospitalStock.setPlatelets(hospitalStock.getPlatelets() + units);
                break;
            case "cryop":
                hospitalStock.setCryoprecipitate(hospitalStock.getCryoprecipitate() + units);
                break;
            case "ffp":
                hospitalStock.setFfp(hospitalStock.getFfp() + units);
                break;
            default:
        }
        return hospitalStock;
    }


    public MainStock createMainStockForTheDay(Date date, String productType, Integer units) {
        Date previousDate = DateUtils.addDays(date, -1);
        MainStock oldmainStock = mainStockRepository.findByDate(previousDate);
        MainStock newMainStock = new MainStock();
        newMainStock.setONegative(oldmainStock == null ? 0 : oldmainStock.getONegative());
        newMainStock.setOPlus(oldmainStock == null ? 0 : oldmainStock.getOPlus());
        newMainStock.setANegative(oldmainStock == null ? 0 : oldmainStock.getANegative());
        newMainStock.setAPlus(oldmainStock == null ? 0 : oldmainStock.getAPlus());
        newMainStock.setBNegative(oldmainStock == null ? 0 : oldmainStock.getBNegative());
        newMainStock.setBPlus(oldmainStock == null ? 0 : oldmainStock.getBPlus());
        newMainStock.setAbNegative(oldmainStock == null ? 0 : oldmainStock.getAbNegative());
        newMainStock.setAbPlus(oldmainStock == null ? 0 : oldmainStock.getAbPlus());
        newMainStock.setPlasma(oldmainStock == null ? 0 : oldmainStock.getPlasma());
        newMainStock.setPlatelets(oldmainStock == null ? 0 : oldmainStock.getPlatelets());
        newMainStock.setCryoprecipitate(oldmainStock == null ? 0 : oldmainStock.getCryoprecipitate());
        newMainStock.setFfp(oldmainStock == null ? 0 : oldmainStock.getFfp());
        newMainStock.setDate(date);
        switch(productType)
        {
            case "oNeg":
                newMainStock.setONegative(newMainStock.getONegative() + units);
                break;
            case "oPls":
                newMainStock.setOPlus(newMainStock.getOPlus() + units);
                break;
            case "aNeg":
                newMainStock.setANegative(newMainStock.getANegative() + units);
                break;
            case "aPls":
                newMainStock.setAPlus(newMainStock.getAPlus() + units);
                break;
            case "bNeg":
                newMainStock.setBNegative(newMainStock.getBNegative() + units);
                break;
            case "bPls":
                newMainStock.setBPlus(newMainStock.getBPlus() + units);
                break;
            case "abNeg":
                newMainStock.setAbNegative(newMainStock.getAbNegative() + units);
                break;
            case "abPls":
                newMainStock.setAbPlus(newMainStock.getAbPlus() + units);
                break;
            case "plasma":
                newMainStock.setPlasma(newMainStock.getPlasma() + units);
                break;
            case "platelets":
                newMainStock.setPlatelets(newMainStock.getPlatelets() + units);
                break;
            case "cryop":
                newMainStock.setCryoprecipitate(newMainStock.getCryoprecipitate() + units);
                break;
            case "ffp":
                newMainStock.setFfp(newMainStock.getFfp() + units);
                break;
            default:
        }
        return newMainStock;
    }

    public MainStock updateMainStock(MainStock mainStock, String productType, int units) {
        switch(productType)
        {
            case "oNeg":
                mainStock.setONegative(mainStock.getONegative() + units);
                break;
            case "oPls":
                mainStock.setOPlus(mainStock.getOPlus() + units);
                break;
            case "aNeg":
                mainStock.setANegative(mainStock.getANegative() + units);
                break;
            case "aPls":
                mainStock.setAPlus(mainStock.getAPlus() + units);
                break;
            case "bNeg":
                mainStock.setBNegative(mainStock.getBNegative() + units);
                break;
            case "bPls":
                mainStock.setBPlus(mainStock.getBPlus() + units);
                break;
            case "abNeg":
                mainStock.setAbNegative(mainStock.getAbNegative() + units);
                break;
            case "abPls":
                mainStock.setAbPlus(mainStock.getAbPlus() + units);
                break;
            case "plasma":
                mainStock.setPlasma(mainStock.getPlasma() + units);
                break;
            case "platelets":
                mainStock.setPlatelets(mainStock.getPlatelets() + units);
                break;
            case "cryop":
                mainStock.setCryoprecipitate(mainStock.getCryoprecipitate() + units);
                break;
            case "ffp":
                mainStock.setFfp(mainStock.getFfp() + units);
                break;
            default:
        }
        return mainStock;
    }


    public HospitalStock createNewStockForTheDayAndReduce(Date date, String hospitalId, String productType, Integer units) {
        Date previousDate = DateUtils.addDays(date, -1);
        HospitalStock hospitalStock = hospitalStockRepository.findByDateAndHospitalId(previousDate, hospitalId);
        hospitalStock.setId(null);
        hospitalStock.setDate(date);
        hospitalStock.setHospitalId(hospitalId);
        switch(productType)
        {
            case "oNeg":
                hospitalStock.setONegative(hospitalStock.getONegative() - units);
                break;
            case "oPls":
                hospitalStock.setOPlus(hospitalStock.getOPlus() - units);
                break;
            case "aNeg":
                hospitalStock.setANegative(hospitalStock.getANegative() - units);
                break;
            case "aPls":
                hospitalStock.setAPlus(hospitalStock.getAPlus() - units);
                break;
            case "bNeg":
                hospitalStock.setBNegative(hospitalStock.getBNegative() - units);
                break;
            case "bPls":
                hospitalStock.setBPlus(hospitalStock.getBPlus() - units);
                break;
            case "abNeg":
                hospitalStock.setAbNegative(hospitalStock.getAbNegative() - units);
                break;
            case "abPls":
                hospitalStock.setAbPlus(hospitalStock.getAbPlus() - units);
                break;
            case "plasma":
                hospitalStock.setPlasma(hospitalStock.getPlasma() - units);
                break;
            case "platelets":
                hospitalStock.setPlatelets(hospitalStock.getPlatelets() - units);
                break;
            case "cryop":
                hospitalStock.setCryoprecipitate(hospitalStock.getCryoprecipitate() - units);
                break;
            case "ffp":
                hospitalStock.setFfp(hospitalStock.getFfp() - units);
                break;
            default:
        }
        return hospitalStock;
    }

    public HospitalStock reduceStock(HospitalStock hospitalStock, String productType, int units) {
        switch(productType)
        {
            case "oNeg":
                hospitalStock.setONegative(hospitalStock.getONegative() - units);
                break;
            case "oPls":
                hospitalStock.setOPlus(hospitalStock.getOPlus() - units);
                break;
            case "aNeg":
                hospitalStock.setANegative(hospitalStock.getANegative() - units);
                break;
            case "aPls":
                hospitalStock.setAPlus(hospitalStock.getAPlus() - units);
                break;
            case "bNeg":
                hospitalStock.setBNegative(hospitalStock.getBNegative() - units);
                break;
            case "bPls":
                hospitalStock.setBPlus(hospitalStock.getBPlus() - units);
                break;
            case "abNeg":
                hospitalStock.setAbNegative(hospitalStock.getAbNegative() - units);
                break;
            case "abPls":
                hospitalStock.setAbPlus(hospitalStock.getAbPlus() - units);
                break;
            case "plasma":
                hospitalStock.setPlasma(hospitalStock.getPlasma() - units);
                break;
            case "platelets":
                hospitalStock.setPlatelets(hospitalStock.getPlatelets() - units);
                break;
            case "cryop":
                hospitalStock.setCryoprecipitate(hospitalStock.getCryoprecipitate() - units);
                break;
            case "ffp":
                hospitalStock.setFfp(hospitalStock.getFfp() - units);
                break;
            default:
        }
        return hospitalStock;
    }


    public MainStock createMainStockForTheDayAndReduce(Date date, String productType, Integer units) {
        Date previousDate = DateUtils.addDays(date, -1);
        MainStock mainStock = mainStockRepository.findByDate(previousDate);
        mainStock.setDate(date);
        mainStock.setId(null);
        switch(productType)
        {
            case "oNeg":
                mainStock.setONegative(mainStock.getONegative() - units);
                break;
            case "oPls":
                mainStock.setOPlus(mainStock.getOPlus() - units);
                break;
            case "aNeg":
                mainStock.setANegative(mainStock.getANegative() - units);
                break;
            case "aPls":
                mainStock.setAPlus(mainStock.getAPlus() - units);
                break;
            case "bNeg":
                mainStock.setBNegative(mainStock.getBNegative() - units);
                break;
            case "bPls":
                mainStock.setBPlus(mainStock.getBPlus() - units);
                break;
            case "abNeg":
                mainStock.setAbNegative(mainStock.getAbNegative() - units);
                break;
            case "abPls":
                mainStock.setAbPlus(mainStock.getAbPlus() - units);
                break;
            case "plasma":
                mainStock.setPlasma(mainStock.getPlasma() - units);
                break;
            case "platelets":
                mainStock.setPlatelets(mainStock.getPlatelets() - units);
                break;
            case "cryop":
                mainStock.setCryoprecipitate(mainStock.getCryoprecipitate() - units);
                break;
            case "ffp":
                mainStock.setFfp(mainStock.getFfp() - units);
                break;
            default:
        }
        return mainStock;
    }

    public MainStock reduceMainStock(MainStock mainStock, String productType, int units) {
        switch(productType)
        {
            case "oNeg":
                mainStock.setONegative(mainStock.getONegative() - units);
                break;
            case "oPls":
                mainStock.setOPlus(mainStock.getOPlus() - units);
                break;
            case "aNeg":
                mainStock.setANegative(mainStock.getANegative() - units);
                break;
            case "aPls":
                mainStock.setAPlus(mainStock.getAPlus() - units);
                break;
            case "bNeg":
                mainStock.setBNegative(mainStock.getBNegative() - units);
                break;
            case "bPls":
                mainStock.setBPlus(mainStock.getBPlus() - units);
                break;
            case "abNeg":
                mainStock.setAbNegative(mainStock.getAbNegative() - units);
                break;
            case "abPls":
                mainStock.setAbPlus(mainStock.getAbPlus() - units);
                break;
            case "plasma":
                mainStock.setPlasma(mainStock.getPlasma() - units);
                break;
            case "platelets":
                mainStock.setPlatelets(mainStock.getPlatelets() - units);
                break;
            case "cryop":
                mainStock.setCryoprecipitate(mainStock.getCryoprecipitate() - units);
                break;
            case "ffp":
                mainStock.setFfp(mainStock.getFfp() - units);
                break;
            default:
        }
        return mainStock;
    }

    public int getValueOfProduct(HospitalStock hospitalStock, String productType) {
        int quantity = 0;
        switch(productType)
        {
            case "oNeg":
                quantity = hospitalStock.getONegative();
                break;
            case "oPls":
                quantity = hospitalStock.getOPlus();
                break;
            case "aNeg":
                quantity = hospitalStock.getANegative();
                break;
            case "aPls":
                quantity = hospitalStock.getAPlus();
                break;
            case "bNeg":
                quantity = hospitalStock.getBNegative();
                break;
            case "bPls":
                quantity = hospitalStock.getBPlus();
                break;
            case "abNeg":
                quantity = hospitalStock.getAbNegative();
                break;
            case "abPls":
                quantity = hospitalStock.getAbPlus();
                break;
            case "plasma":
                quantity = hospitalStock.getPlasma();
                break;
            case "platelets":
                quantity = hospitalStock.getPlatelets();
                break;
            case "cryop":
                quantity = hospitalStock.getCryoprecipitate();
                break;
            case "ffp":
                quantity = hospitalStock.getFfp();
                break;
            default:
        }
        return quantity;
    }


    public String getProductToString(String productType) {
        String value = productType;
        switch(productType)
        {
            case "oNeg":
                value = "O-";
                break;
            case "oPls":
                value = "O+";
                break;
            case "aNeg":
                value = "A-";
                break;
            case "aPls":
                value = "A+";
                break;
            case "bNeg":
                value = "B-";
                break;
            case "bPls":
                value = "B+";
                break;
            case "abNeg":
                value = "AB-";
                break;
            case "abPls":
                value = "AB+";
                break;
            case "plasma":
                value = "plasma";
                break;
            case "platelets":
                value = "platelets";
                break;
            case "cryop":
                value = "cryoprecipitate";
                break;
            case "ffp":
                value = "ffp";
                break;
            default:
        }
        return value;
    }

    public String getCorrectProductToShort(String productType) {
        String value = productType;
        switch(productType)
        {
            case "O-":
                value = "oNeg";
                break;
            case "oPlsO+":
                value = "oPls";
                break;
            case "A-":
                value = "aNeg";
                break;
            case "A+":
                value = "aPls";
                break;
            case "B-":
                value = "bNeg";
                break;
            case "B+":
                value = "bPls";
                break;
            case "AB-":
                value = "abNeg";
                break;
            case "AB+":
                value = "abPls";
                break;
            case "plasma":
                value = "plasma";
                break;
            case "platelets":
                value = "platelets";
                break;
            case "cryoprecipitate":
                value = "cryop";
                break;
            case "ffp":
                value = "ffp";
                break;
            default:
        }
        return value;
    }

}
