package com.univice.cse364project.user;

import com.univice.cse364project.device.Device;
import com.univice.cse364project.inquiry.Inquiry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String password;
    private String email;
    private String studentId;
    private boolean isAdmin;
    private List<String> waitingType;
    private Device currentUsingDevice;
    private List<Inquiry> writtenInquiries;
}
