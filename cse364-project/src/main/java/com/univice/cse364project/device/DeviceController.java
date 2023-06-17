package com.univice.cse364project.device;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.Generated;
import com.univice.cse364project.user.User;
import com.univice.cse364project.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/")
public class DeviceController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Generated
    public DeviceController(DeviceRepository deviceRepository, UserRepository userRepository) throws IOException, CsvException {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        long size = deviceRepository.count();
        if(size == 0) {
            LOG.info("Loading devices");
            readDataFromCsv("device.csv");
        }
    }
    @RequestMapping(value="/devices", method = RequestMethod.GET)
    public List<Device> getAllDevices() {
        List<Device> total = deviceRepository.findAll();
        for (Device d : total) { // 목록을 가져올 때마다 날짜 지난 기기 체크
            if(d.getEndDate()==null) continue;
            if (d.getEndDate().isBefore(LocalDate.now())){
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("id").is(d.getCurrentUser()));
                User currentUser = mongoTemplate.findOne(query1, User.class);
                currentUser.setCurrentUsingDevice(null);
                userRepository.save(currentUser);
                d.setStartDate(null);
                d.setEndDate(null);
                d.setCurrentUser(null);
                deviceRepository.save(d);
            }
        }
        return total;
    }
    @RequestMapping(value = "/devices/{type}", method = RequestMethod.GET)
    public List<Device> getAllTypeDevices(@PathVariable String type) {
        List<Device> total = getAllDevices();
        List<Device> target = new ArrayList<>();
        for (Device d : total) {
            if (d.getType().equals(type)) {
                target.add(d);
            }
        }
        return target;
    }
    @PutMapping(value="/device/rent/{id}")
    public Device rentDevice(@RequestParam Map<String, Object> map, @PathVariable String id) {
        String authId = (String) map.get("authenticationId");
        User u = userRepository.findById(authId).orElse(null);
        if(u==null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        if(u.getCurrentUsingDevice()!=null){
            // 기기 한 대 더 빌리려는 경우
            throw new MoreDeviceException();
        }
        Device device = deviceRepository.findById(id).orElse(null);
        if(device==null) {
            // 기기 아이디가 틀린 경우
            throw new NoDeviceException();
        }
        if(device.getCurrentUser()!=null) {
            // 기기가 빌린 상태인 경우
            throw new RentedDeviceException();
        }
        device.setStartDate(LocalDate.now());
        device.setEndDate(LocalDate.now().plusMonths(3));
        device.setCurrentUser(u.getId());
        deviceRepository.save(device);
        u.setCurrentUsingDevice(device);
        userRepository.save(u);
        return device;
    }
    @PutMapping(value = "/device/return/{id}")
    public Device returnDevice(@RequestParam Map<String, Object> map, @PathVariable String id) {
        String authId = (String) map.get("authenticationId");
        User u = userRepository.findById(authId).orElse(null);
        if(u==null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        if(u.getCurrentUsingDevice()==null){
            // 빌린 기기가 없는 경우
            throw new NoRentedDeviceException();
        }
        Device device = deviceRepository.findById(id).orElse(null);
        if(device==null) {
            // 기기 아이디가 틀린 경우
            throw new NoDeviceException();
        }
        if(!Objects.equals(device.getCurrentUser(), u.getId())) {
            // 유저가 빌린 기기가 아닌 경우
            throw new WrongRentedDeviceException();
        }
        u.setCurrentUsingDevice(null);
        userRepository.save(u);
        device.setStartDate(null);
        device.setEndDate(null);
        device.setCurrentUser(null);
        deviceRepository.save(device);
        return device;
    }
    @Generated
    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Device data = new Device();
            data.setId(nextLine[0]);
            data.setName(nextLine[1]);
            data.setType(nextLine[2]);
            if(!Objects.equals(nextLine[5], "")) {
                LocalDate startDate = LocalDate.parse(nextLine[3], DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDate endDate = LocalDate.parse(nextLine[4], DateTimeFormatter.ofPattern("yyyyMMdd"));
                data.setStartDate(startDate);
                data.setEndDate(endDate);
                data.setCurrentUser(nextLine[5]);
            }
            deviceRepository.save(data);
        }
    }
    @ExceptionHandler(WrongAuthenticationIdException.class)
    public DeviceError WrongAuthenticationIdExceptionHandler(WrongAuthenticationIdException e) {
        return new DeviceError("AuthenticationId is wrong.");
    }
    @ExceptionHandler(NoDeviceException.class)
    public DeviceError NoDeviceExceptionHandler(NoDeviceException e) {
        return new DeviceError("There is no device using given id.");
    }
    @ExceptionHandler(RentedDeviceException.class)
    public DeviceError RentedDeviceExceptionHandler(RentedDeviceException e) {
        return new DeviceError("This device is already rented.");
    }
    @ExceptionHandler(MoreDeviceException.class)
    public DeviceError MoreDeviceExceptionHandler(MoreDeviceException e) {
        return new DeviceError("You can rent only one device.");
    }
    @ExceptionHandler(NoRentedDeviceException.class)
    public DeviceError NoRentedDeviceExceptionHandler(NoRentedDeviceException e) {
        return new DeviceError("You have no rented device.");
    }
    @ExceptionHandler(WrongRentedDeviceException.class)
    public DeviceError WrongRentedDeviceExceptionHandler(WrongRentedDeviceException e) {
        return new DeviceError("This device is not your rented device.");
    }
}
