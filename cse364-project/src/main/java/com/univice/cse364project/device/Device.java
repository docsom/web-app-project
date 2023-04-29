package com.univice.cse364project.device;

import com.univice.cse364project.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="device")
public class Device {
    @Id
    private String id;
    private String name;
    private String type;
    private Date startDate;
    private Date endDate;
    private User currentUser;
}
