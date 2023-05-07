package com.univice.cse364project.dormResident;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dorm-resident")
public class DormResident {
    @Id
    private String studentNumber;
    private String idNumber;

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
