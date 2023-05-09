package com.univice.cse364project.user;

import com.univice.cse364project.device.Device;
import com.univice.cse364project.inquiry.Inquiry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user")
public class User {
    @Id
    private String authenticationId;
    private String id;
    private String password;
    private String email;
    private String studentId;
    private boolean isAdmin;
    private List<String> waitingType;
    private Device currentUsingDevice;
    private List<Inquiry> writtenInquiries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<String> getWaitingType() {
        return waitingType;
    }

    public void setWaitingType(List<String> waitingType) {
        this.waitingType = waitingType;
    }

    public Device getCurrentUsingDevice() {
        return currentUsingDevice;
    }

    public void setCurrentUsingDevice(Device currentUsingDevice) {
        this.currentUsingDevice = currentUsingDevice;
    }

    public List<Inquiry> getWrittenInquiries() {
        return writtenInquiries;
    }

    public void setWrittenInquiries(List<Inquiry> writtenInquiries) {
        this.writtenInquiries = writtenInquiries;
    }
}
