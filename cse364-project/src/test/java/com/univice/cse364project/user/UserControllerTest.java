package com.univice.cse364project.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Test
    @DisplayName("Register new user with invalid student id")
    void registerNewUserWithInvalidStudentId() {
        //given
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "kicker5236");
        requestBody.put("password", "Kicker6325!");
        requestBody.put("email", "kicker5236@unist.ac.kr");
        requestBody.put("studentId", "20181016");
        requestBody.put("idNum","1111111111111");
        //when
        //then
        assertThrows(InvalidStudentIdException.class, ()->userController.registerNewUser(requestBody));
    }

    @Test
    @DisplayName("Register new user with existing id")
    void registerNewUserWithExistingId() {
        //given
        ObjectMapper om = new ObjectMapper();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "aaa");
        requestBody.put("password", "Kicker6325!");
        requestBody.put("email", "kicker5236@unist.ac.kr");
        requestBody.put("studentId", "20181111");
        requestBody.put("idNum","1111111111111");
        //when
        //then
        assertThrows(ExistingIdException.class, ()->userController.registerNewUser(requestBody));
    }

    @Test
    @DisplayName("Register new user with existing student id")
    void registerNewUserWithExistingStudentId() {
        //given
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "kicker5236");
        requestBody.put("password", "Kicker6325!");
        requestBody.put("email", "kicker5236@unist.ac.kr");
        requestBody.put("studentId", "20181234");
        requestBody.put("idNum","1111111111111");
        //when
        //then
        assertThrows(ExistingStudentIdException.class, ()->userController.registerNewUser(requestBody));
    }

    @Test
    @DisplayName("Register new user with unmatched id number")
    void registerNewUserWithUnmatchedIdNumber() {
        //given

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "kicker5236");
        requestBody.put("password", "Kicker6325!");
        requestBody.put("email", "kicker5236@unist.ac.kr");
        requestBody.put("studentId", "20181111");
        requestBody.put("idNum","1111111111112");
        //when
        //then
        assertThrows(UnmatchedIdNumberException.class, ()->userController.registerNewUser(requestBody));
    }

    @Test
    @DisplayName("Register new user")
    void registerNewUser() throws JsonProcessingException {

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "kym20181016");
        requestBody.put("password", "kym000131!");
        requestBody.put("email", "kym20181016@unist.ac.kr");
        requestBody.put("studentId", "20192222");
        requestBody.put("idNum","2222222222222");

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("kym20181016"));

        User before = mongoTemplate.findOne(query, User.class);
        //when
        userController.registerNewUser(requestBody);
        //then
        User after = mongoTemplate.findOne(query, User.class);
        assertTrue(before == null);
        assertTrue(after != null && after instanceof User);
    }

    @Test
    @DisplayName("login with non existing id")
    void LoginWithNonExistingId() {
        //given
        ObjectMapper om = new ObjectMapper();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "ddd1234");
        requestBody.put("password","Ddd4321");
        //when
        //then
        assertThrows(NoUserException.class, ()->userController.LoginUser(requestBody));
    }

    @Test
    @DisplayName("login with wrong password")
    void LoginWithWrongPassword() {
        //given
        ObjectMapper om = new ObjectMapper();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "aaa");
        requestBody.put("password","4321");
        //when
        //then
        assertThrows(WrongPasswordException.class, ()->userController.LoginUser(requestBody));
    }

    @Test
    @DisplayName("login user")
    void LoginUser() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "aaa");
        requestBody.put("password","1234");
        //when
        String result = userController.LoginUser(requestBody);
        //then
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("aaa"));
        assertTrue(result.equals(mongoTemplate.findOne(query, User.class).getAuthenticationId()));
    }

    @AfterAll
    void afterAll() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("kym20181016"));
        User testUser = mongoTemplate.findOne(query, User.class);
        mongoTemplate.remove(testUser);
    }
}