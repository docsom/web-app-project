package com.univice.cse364project.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.univice.cse364project.user.User;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceControllerTest {
    @Autowired
    private DeviceController deviceController;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DeviceRepository deviceRepository;
    @BeforeAll
    void beforeTest() {
        User testUser = new User();
        testUser.setId("test");
        Device testDevice = new Device();
        testDevice.setId("test");
        testDevice.setType("test");
        mongoTemplate.insert(testUser, "user");
        mongoTemplate.insert(testDevice, "device");
    }
    @Test
    @DisplayName("Check all end date of devices")
    void checkEndDate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("test"));
        Device d = mongoTemplate.findOne(query, Device.class);
        d.setEndDate(LocalDate.of(2022,5,15));
        d.setCurrentUser("test");
        mongoTemplate.save(d);
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
        assertEquals(devices.size(), deviceRepository.count());
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
        query.addCriteria(Criteria.where("id").is("test"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        Device d = mongoTemplate.findOne(query, Device.class);
        u.setCurrentUsingDevice(d);
        mongoTemplate.save(u);
        //then
        assertThrows(MoreDeviceException.class, ()->deviceController.rentDevice(requestBody, "device2"));
        u.setCurrentUsingDevice(null);
        mongoTemplate.save(u);
    }
    @Test
    @DisplayName("Rent wrong id device")
    void rentWrongIdDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("test"));
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
        query.addCriteria(Criteria.where("currentUsingDevice").is(null));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("id").is("test"));
        Device d = mongoTemplate.findOne(query2, Device.class);
        d.setCurrentUser("test");
        mongoTemplate.save(d);
        //then
        assertThrows(RentedDeviceException.class, ()->deviceController.rentDevice(requestBody, "test"));
        d.setCurrentUser(null);
        mongoTemplate.save(d);
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
        query.addCriteria(Criteria.where("id").is("test"));
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
        query.addCriteria(Criteria.where("id").is("test"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        Device d = mongoTemplate.findOne(query, Device.class);
        u.setCurrentUsingDevice(d);
        mongoTemplate.save(u);
        //then
        assertThrows(NoDeviceException.class, ()->deviceController.returnDevice(requestBody, "device5"));
        u.setCurrentUsingDevice(null);
        mongoTemplate.save(u);
    }
    @Test
    @DisplayName("Return wrong rented device")
    void returnWrongRentedDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("test"));
        User u = mongoTemplate.findOne(query, User.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        Device d = mongoTemplate.findOne(query, Device.class);
        d.setCurrentUser("test2");
        u.setCurrentUsingDevice(d);
        mongoTemplate.save(u);
        mongoTemplate.save(d);
        //then
        assertThrows(WrongRentedDeviceException.class, ()->deviceController.returnDevice(requestBody, "test"));
        d.setCurrentUser(null);
        u.setCurrentUsingDevice(null);
        mongoTemplate.save(u);
        mongoTemplate.save(d);
    }
    @Test
    @DisplayName("Rent and Return correctly")
    void rentAndReturnDevice() {
        //given
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("test"));
        User u = mongoTemplate.findOne(query, User.class);
        Device d = mongoTemplate.findOne(query, Device.class);
        String authId = u.getAuthenticationId();
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId", authId);
        //when
        deviceController.rentDevice(requestBody, "test");
        //then
        u = mongoTemplate.findOne(query, User.class);
        d = mongoTemplate.findOne(query, Device.class);
        assertEquals(d.getCurrentUser(), u.getId());
        //when
        deviceController.returnDevice(requestBody, "test");
        //then
        d = mongoTemplate.findOne(query, Device.class);
        assertNull(d.getCurrentUser());
    }
    @AfterAll
    void afterTest() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("test"));
        User u = mongoTemplate.findOne(query, User.class);
        Device d = mongoTemplate.findOne(query, Device.class);
        mongoTemplate.remove(u);
        mongoTemplate.remove(d);
    }
}
