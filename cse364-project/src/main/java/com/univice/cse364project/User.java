package com.univice.cse364project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    private Long userId;
    private String gender;
    private int age;
    private int occupation;

    private String zipcode;

    public Long getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getOccupation() {
        return occupation;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
