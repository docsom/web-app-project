package com.univice.cse364project.inquiry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InquiryControllerTest {
    @Autowired
    private InquiryController inquiryController;
    @Test
    @DisplayName("Write new board with sufficient ")
    void NewboardwithSufficientId() {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry1");
        inquiry.put("title", "chantest1!");
        inquiry.put("contents", "invalide board");
        inquiry.put("isConfirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId","1111111111111");
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
        inquiry.put("isConfirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry",inquiry);
        requestBody.put("idNum","1111111111111");

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("inquiry8"));

        //when
        userController.insertBoard(requestBody);
        //then
        Inquiry newboard = mongoTemplate.findOne(query, Inquiry.class);
        assertTrue(newboard != null && after instanceof newboard);

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
        inquiry.put("isConfirmed", false);
        inquiry.put("writer", "22222222222");
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId","1111111111111");


        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.editboard(requestBody,"inquiry8"));
    }

    @Test
    @DisplayName("Put board with invalid")
    void PutboardwithInsufficientauthority() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId","1111111111111");

        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.editboard(requestBody,"inquiry8"));
    }

    @Test
    @DisplayName("Edit board")
    void editboard() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry8");
        inquiry.put("title", "chantestischange");
        inquiry.put("contents", " boardischanged");
        inquiry.put("isConfirmed", false);
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry",inquiry);
        requestBody.put("idNum","1111111111111");
        string beforecontents = "board";
        string beforetitle = "chantest8!";
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("inquiry8"));

        //when
        userController.editboard(requestBody, "inquiry8");
        //then
        Inquiry newboard = mongoTemplate.findOne(query, Inquiry.class);
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
        requestBody.put("authenticationId","1111111111111");

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
        requestBody.put("authenticationId","1111111111111");

        //when
        //then
        assertThrows(InvalidInquiryException.class, ()->inquiryController.Solved(requestBody,"inquiry2"));
    }

    @Test
    @DisplayName("Board is confirmed")
    void Solved() throws JsonProcessingException {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("idNum","1111111111111");
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("inquiry8"));
        boolean before = false;
        //when
        userController.insertBoard(requestBody);
        //then
        Inquiry newboard = mongoTemplate.findOne(query, Inquiry.class);
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
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.deleteBoard(requestBody, "inquiry8"));
    }

    @Test
    @DisplayName("Delete board with invalid")
    void DeleteboardwithInsufficientauthority() {
        //given
        ObjectMapper om = new ObjectMapper();
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("authenticationId","1111111111111");

        //when
        //then
        assertThrows(InsufficientpermissionException.class, ()->inquiryController.deleteBoard(requestBody, "inquiry8"));
    }


    @Test
    @DisplayName("Delete Board")
    void deleteBoard() throws JsonProcessingException {
        //given


        requestBody.put("idNum","1111111111111");

        Query query = new Query();
        query.addCriteria(Criteria.where("writer").is("1111111111111"));

        //when
        userController.insertBoard(requestBody, "inquiry8");
        //then
        Inquiry deleted = mongoTemplate.findOne(query, Inquiry.class);
        assertTrue(deleted == null);

    }


}