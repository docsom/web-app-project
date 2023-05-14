package com.univice.cse364project.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.Generated;
import com.univice.cse364project.dormResident.DormResident;
import com.univice.cse364project.dormResident.DormResidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

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

    @Generated
    public UserController(UserRepository userRepository, DormResidentRepository dormResidentRepository) throws IOException, CsvException {
        this.userRepository = userRepository;
        this.dormResidentRepository = dormResidentRepository;

        long size = userRepository.count();
        if(size == 0) {
            LOG.info("Loading users");
            readDataFromCsv("user.csv");
        }
    }

    @Generated
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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String LoginUser(@RequestBody ObjectNode saveObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String id = saveObj.get("id").asText();
        String password = saveObj.get("password").asText();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(id));
        User existingUser = mongoTemplate.findOne(query1, User.class);

        if(existingUser == null){
            throw new NoUserException();
        } else {
            if(existingUser.getPassword().equals(password)){
                return existingUser.getAuthenticationId();
            } else {
                throw new WrongPasswordException();
            }
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registerNewUser(@RequestBody ObjectNode saveObj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.treeToValue(saveObj.get("user"), User.class);
        String idNum = saveObj.get("idNum").asText();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(user.getId()));
        User existingUser = mongoTemplate.findOne(query1, User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("studentNumber").is(user.getStudentId()));
        DormResident existingResident = mongoTemplate.findOne(query2, DormResident.class);

        Query query3 = new Query();
        query3.addCriteria(Criteria.where("studentId").is(user.getStudentId()));
        User existingUserExistingStudentId = mongoTemplate.findOne(query3, User.class);


        if(existingUser != null){
            //해당 id가 이미 사용중인 경우
            throw new ExistingIdException();
        }
        else if(existingResident == null) {
            //기숙사 거주자 학번이 아닌 경우
            throw new InvalidStudentIdException();
        }
        else if( existingUserExistingStudentId != null ){
            //해당 학번으로 만든 계정이 있는 경우
            throw new ExistingStudentIdException();
        } else if(existingResident != null && !existingResident.getIdNumber().equals(idNum)){
            //주민등록번호가 학번과 일치하지 않는 경우
            throw new UnmatchedIdNumberException();
        } else {
            mongoTemplate.save(user);
            return userRepository.save(user);
        }
    }

    @ExceptionHandler(ExistingIdException.class)
    public UserError ExistingIdExceptionHandler(ExistingIdException e){
        return new UserError("Given id is already used.");
    }

    @ExceptionHandler(ExistingStudentIdException.class)
    public UserError ExistingStudentIdExceptionHandler(ExistingStudentIdException e){
        return new UserError("Given student id is already used.");
    }

    @ExceptionHandler(InvalidStudentIdException.class)
    public UserError InvalidStudentIdExceptionHandler(InvalidStudentIdException e){
        return new UserError("Given student id is invalid.");
    }

    @ExceptionHandler(UnmatchedIdNumberException.class)
    public UserError UnmatchedIdNumberExceptionHandler(UnmatchedIdNumberException e){
        return new UserError("Given Id number is not matched.");
    }

    @ExceptionHandler(NoUserException.class)
    public UserError NoUserExceptionHandler(NoUserException e){
        return new UserError("There is no user using given id.");
    }

    @ExceptionHandler(WrongPasswordException.class)
    public UserError WrongPasswordExceptionHandler(WrongPasswordException e){
        return new UserError("Password is wrong.");
    }
}
