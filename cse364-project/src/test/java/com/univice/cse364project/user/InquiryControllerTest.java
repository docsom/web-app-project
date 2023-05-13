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
    void wrongregisterNewUser() {
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
    @DisplayName("Write new board with invalid")
    void registerNewUser() {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode inquiry = om.createObjectNode();
        inquiry.put("id", "inquiry2");
        inquiry.put("title", "chantest2!");
        inquiry.put("contents", "can make board");
        inquiry.put("isConfirmed", false);
        inquiry.put("writer", "22222222222");
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("inquiry", inquiry);
        requestBody.put("authenticationId","1111111111111");//근데 이거 로컬마다 다를텐데 어떻게 고정하지?

        //when
        //then
        assertThrows(WrongAuthenticationIdException.class, ()->inquiryController.insertBoard(requestBody));
    }
}