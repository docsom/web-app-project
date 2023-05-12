package com.univice.cse364project.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;
    @Test
    @DisplayName("Register new user with invalid")
    void registerNewUser() {
        //given
        ObjectMapper om = new ObjectMapper();

        ObjectNode user = om.createObjectNode();
        user.put("id", "kicker5236");
        user.put("password", "Kicker6325!");
        user.put("email", "kicker5236@unist.ac.kr");
        user.put("studentId", "20181016");
        ObjectNode requestBody = om.createObjectNode();
        requestBody.put("user", user);
        requestBody.put("idNum","1111111111111");
        //when
        //then
        assertThrows(InvalidStudentIdException.class, ()->userController.registerNewUser(requestBody));
    }
}