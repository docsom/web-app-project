package com.univice.cse364project.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.univice.cse364project.user.User;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class DeviceControllerTest {
    @Autowired
    private DeviceController deviceController;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Test
    @DisplayName("Check all end date of devices")
    void checkEndDate() {
        List<Device> devices = deviceController.getAllDevices();
        for (Device device:devices) {
            if(device.getEndDate()==null) continue;
            assertFalse(device.getEndDate().isBefore(LocalDate.now()));
        }
    }
    @Test
    @DisplayName("Check num of devices is 4")
    void checkNumDevices() {
        List<Device> devices = deviceController.getAllDevices();
        assertEquals(devices.size(), 4);
    }
    @Test
    @DisplayName("Show monitor devices")
    void GetMonitorDevices() {
        List<Device> monitors = deviceController.getAllTypeDevices("monitor");
        assertEquals(monitors.size(), 2);
    }
    @Test
    @DisplayName("Show wrong Type devices")
    void GetWrongTypeDevices() {
        List<Device> speakers = deviceController.getAllTypeDevices("speaker");
        assertEquals(speakers.size(), 0);
    }
    @Test
    @DisplayName("Rent with wrong authentication id")
    void rentWithWrongAuthenticationId() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", "");
        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->deviceController.rentDevice(requestBody, "device2"));
    }
    @Test
    @DisplayName("Rent one more device")
    void rentMoreDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("bbb"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        deviceController.rentDevice(requestBody, "device1");
        //then
        assertThrows(MoreDeviceException.class, ()->deviceController.rentDevice(requestBody, "device2"));
    }
    @Test
    @DisplayName("Rent wrong id device")
    void rentWrongIdDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("aaa"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        //then
        assertThrows(NoDeviceException.class, ()->deviceController.rentDevice(requestBody, "device5"));
    }
    @Test
    @DisplayName("Rent already rented device")
    void rentRentedDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("aaa"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        //then
        assertThrows(RentedDeviceException.class, ()->deviceController.rentDevice(requestBody, "device1"));
    }
    @Test
    @DisplayName("Return with wrong authentication id")
    void returnWithWrongAuthenticationId() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", "");
        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->deviceController.returnDevice(requestBody, "device2"));
    }
    @Test
    @DisplayName("Return when user has no device")
    void returnNoDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("aaa"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        //then
        assertThrows(NoRentedDeviceException.class, ()->deviceController.returnDevice(requestBody, "device2"));
    }
    @Test
    @DisplayName("Return wrong id device")
    void returnWrongIdDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("bbb"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        //then
        assertThrows(NoDeviceException.class, ()->deviceController.returnDevice(requestBody, "device5"));
    }
    @Test
    @DisplayName("Return wrong rented device")
    void returnWrongRentedDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("bbb"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        //then
        assertThrows(WrongRentedDeviceException.class, ()->deviceController.returnDevice(requestBody, "device2"));
    }
}
