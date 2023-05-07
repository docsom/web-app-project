package com.univice.cse364project.user;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.dormResident.DormResident;
import com.univice.cse364project.dormResident.DormResidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping(value = "/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final DormResidentRepository dormResidentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserController(UserRepository userRepository, DormResidentRepository dormResidentRepository) throws IOException, CsvException {
        this.userRepository = userRepository;
        this.dormResidentRepository = dormResidentRepository;

        long size = userRepository.count();
        if(size == 0) {
            LOG.info("Loading users");
            readDataFromCsv("user.csv");
        }
    }
    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            User data = new User();
            data.setId(nextLine[0]);
            data.setPassword(nextLine[1]);
            data.setEmail(nextLine[2]);
            data.setStudentId(nextLine[3]);
            data.setAdmin(Boolean.parseBoolean(nextLine[4]));
            userRepository.save(data);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registerNewUser(@RequestBody User user){

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(user.getId()));
        User existingUser = mongoTemplate.findOne(query1, User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("studentNumber").is(user.getStudentId()));
        DormResident existingResident = mongoTemplate.findOne(query2, DormResident.class);

        Query query3 = new Query();
        query3.addCriteria(Criteria.where("studentId").is(existingResident.getStudentNumber()));
        User existingUserExistingStudentId = mongoTemplate.findOne(query3, User.class);


        if(existingUser != null){
            //해당 id가 이미 사용중인 경우
            System.out.println(existingUser);
        }
        else if(existingResident == null) {
            //기숙사 거주자 학번이 아닌 경우
            System.out.println(existingResident);
        }
        else if( existingUserExistingStudentId != null ){
            //해당 학번으로 만든 계정이 있는 경우
            System.out.println(existingUserExistingStudentId);
        }
        return user;
    }
}
