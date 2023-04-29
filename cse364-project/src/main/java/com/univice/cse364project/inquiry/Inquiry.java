package com.univice.cse364project.inquiry;

import com.univice.cse364project.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inquiry")
public class Inquiry {
    @Id
    private String id;
    private String title;
    private String contents;
    private User writer;
    private boolean isConfirmed;
}
