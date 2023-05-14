package com.univice.cse364project.inquiry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.device.DeviceRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.univice.cse364project.user.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InquiryControllerTest {
    @Autowired
    private InquiryController inquiryController;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private InquiryRepository inquiryRepository;
    @BeforeAll
    void beforeTest() throws IOException, CsvException {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquirytest");
        inquiry.put("title", "testtitle");
        inquiry.put("contents", " testboard");
        inquiry.put("confirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        requestBody.put("inquiry",inquiry);
        requestBody.put("authenticationId",authid);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is("inquirytest"));

        //when
        inquiryController.insertBoard(requestBody);
        //then
        Inquiry newboard = mongoTemplate.findOne(query1, Inquiry.class);
        assertTrue(newboard != null && newboard instanceof Inquiry);


    }
    @Test
    @DisplayName("Write new board with sufficient ")
    void writeboardwithInSufficientId() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry7");
        inquiry.put("title", "chantest1!");
        inquiry.put("contents", "invalide board");
        inquiry.put("confirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId","111");
        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.insertBoard(requestBody));
    }


    @Test
    @DisplayName("Write new board")
    void insertBoard() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry8");
        inquiry.put("title", "chantest8!");
        inquiry.put("contents", " board");
        inquiry.put("confirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();



        requestBody.put("inquiry",inquiry);

        requestBody.put("authenticationId",authid);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is("inquiry8"));

        //when
        inquiryController.insertBoard(requestBody);
        //then
        Inquiry newboard = mongoTemplate.findOne(query1, Inquiry.class);
        assertTrue(newboard != null && newboard instanceof Inquiry);

    }


    @Test
    @DisplayName("Put board with not login")
    void PutboardwithNotlogin() {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry5");
        inquiry.put("title", "chantest5!");
        inquiry.put("contents", "can make board");
        inquiry.put("confirmed", false);
        inquiry.put("writer", "22222222222");
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId","1111111111111");


        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.editboard(requestBody,"inquiry8"));
    }


    @Test
    @DisplayName("Put board with Insufficientauthority")
    void PutboardwithInsufficientauthority() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode inquiry = om.createObjectNode();
        ObjectNode requestBody = om.createObjectNode();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        inquiry.put("id", "inquiry5");
        inquiry.put("title", "chantest5!");
        inquiry.put("contents", "can make board");
        inquiry.put("confirmed", false);

        requestBody.put("authenticationId",authid);

        //when
        //then
        assertThrows(InsufficientpermissionException.class, ()->inquiryController.editboard(requestBody,"inquiry5"));
    }

    @Test
    @DisplayName("Udmin Put board with Insufficientauthority")
    void PutadminboardwithInsufficientauthority() throws JsonProcessingException{
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode inquiry = om.createObjectNode();
        ObjectNode requestBody = om.createObjectNode();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        inquiry.put("id", "inquiry77");
        inquiry.put("title", "chantest5!");
        inquiry.put("contents", "can make board");
        inquiry.put("confirmed", false);
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId",authid);
        Query query1 = new Query();

        //when
        inquiryController.editboard(requestBody, "inquiry3");
        //then
        query1.addCriteria(Criteria.where("id").is("inquirytest"));
        Inquiry newboard = mongoTemplate.findOne(query1, Inquiry.class);

        assertTrue(newboard!=null);
    }

    @Test
    @DisplayName("Edit board")
    void editboard() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        ObjectNode inquiry = om.createObjectNode();

        inquiry.put("id", "inquirytest");
        inquiry.put("title", "chantestischange");
        inquiry.put("contents", " boardischanged");
        inquiry.put("confirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry",inquiry);
        requestBody.put("authenticationId",authid);
        String beforecontents = "testboard";
        String beforetitle = "testtitle";
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is("inquirytest"));

        //when

        inquiryController.editboard(requestBody, "inquirytest");
        //then
        Inquiry newboard = mongoTemplate.findOne(query1, Inquiry.class);
        assertTrue(!beforecontents.equals(newboard.getContents()) || !beforetitle.equals(newboard.getTitle()));
    }

    @Test
    @DisplayName("Put board with not login")
    void PutboardsolvedwithNotlogin() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId","1111111111111");


        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.Solved(requestBody,"inquiry8"));
    }

    @Test
    @DisplayName("Put board with invalid")
    void PutboardsolvedwithInsufficientauthority() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        requestBody.put("authenticationId",authid);

        //when
        //then
        assertThrows(InsufficientpermissionException.class, ()->inquiryController.Solved(requestBody,"inquiry8"));
    }

    @Test
    @DisplayName("Put board with invalid")
    void PutboardsolvedwithThereisnoboard() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();

        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        requestBody.put("authenticationId", authid);

        //when
        //then
        assertThrows(InvalidInquiryException.class, ()->inquiryController.Solved(requestBody,"inquiryno"));
    }

    @Test
    @DisplayName("Board is confirmed")
    void Solved() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();

        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();


        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is("inquiry3"));
        boolean before = false;

        requestBody.put("authenticationId",authid);
        //when
        inquiryController.Solved(requestBody, "inquiry3");
        //then
        Inquiry newboard = mongoTemplate.findOne(query1, Inquiry.class);
        assertTrue(before != newboard.isConfirmed());

    }


    @Test
    @DisplayName("Delete board with not login")
    void DeleteboardwithNotlogin() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId","1111111111111");

        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.deleteBoard(requestBody, "inquirytest"));
    }

    @Test
    @DisplayName("Delete board with invalid")
    void DeleteboardwithInsufficientauthority() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();

        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();
        requestBody.put("authenticationId",authid);
        //when
        //then
        assertThrows(InsufficientpermissionException.class, ()->inquiryController.deleteBoard(requestBody, "inquiry1"));
    }


    @Test
    @DisplayName("Delete Board")
    void deleteBoard() throws JsonProcessingException {
        //given

        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();

        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        User u = mongoTemplate.findOne(query, User.class);
        String authid = u.getAuthenticationId();

        requestBody.put("authenticationId",authid);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("writer").is(authid));

        //when
        inquiryController.deleteBoard(requestBody, "inquirytest");
        //then
        Inquiry deleted = mongoTemplate.findOne(query1, Inquiry.class);
        assertTrue(deleted == null);

    }

    @AfterAll
    void afterAll() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is("chantest8!"));
        Inquiry test = mongoTemplate.findOne(query, Inquiry.class);
        mongoTemplate.remove(test);
    }

}