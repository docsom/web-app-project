package com.univice.cse364project.inquiry;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.univice.cse364project.user.User;
import com.univice.cse364project.user.UserRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value="/")
public class InquiryController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final InquiryRepository InquiryRepository;
    private final UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Generated
    public InquiryController(InquiryRepository inquiryRepository, UserRepository userRepository) throws IOException, CsvException {
        this.InquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
        long size = inquiryRepository.count();
        if (size == 0) {
            LOG.info("Loading inquiries");
            readDataFromCsv("inquiry.csv");
        }
    }


    @RequestMapping(value = "/inquirys", method = RequestMethod.GET)
    public List<Inquiry> getAllInquiry() {
        return InquiryRepository.findAll();
    }//게시글 보여주기

    @RequestMapping(value = "/inquiry/{id}", method = RequestMethod.GET)
    public Inquiry getBoard(@PathVariable String id) {
        LOG.info("Getting Board with Id: {}.", id);
        return InquiryRepository.findById(id).orElse(null);
    }//게시글 찾기

    @RequestMapping(value = "/inquiry/writer/{writer}", method = RequestMethod.GET)
    public List<Inquiry> writerBoard(@PathVariable String writer) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("writer").is(writer));

        List<Inquiry> boardlist = mongoTemplate.find(query1, Inquiry.class);
        LOG.info("Getting Board with Writer: {}.", writer);

        return boardlist;
    }//작성자 게시글 찾기

    @RequestMapping(value = "/inquiry/write", method = RequestMethod.POST)
    public Inquiry insertBoard(@RequestBody ObjectNode saveObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Inquiry inquiry = mapper.treeToValue(saveObj.get("inquiry"), Inquiry.class);
        String authId = saveObj.get("authenticationId").asText();
        User u = userRepository.findById(authId).orElse(null);
        if (u == null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        inquiry.setWriter(u.getId());
        return InquiryRepository.save(inquiry);
    }//게시글 작성


    @RequestMapping(value = "/inquiry/{id}", method = RequestMethod.DELETE)
    public void deleteBoard(@RequestBody ObjectNode saveObj, @PathVariable String id) throws JsonProcessingException {
        String authId = saveObj.get("authenticationId").asText();
        Inquiry thisboard = InquiryRepository.findById(id).orElse(null);
        User u = userRepository.findById(authId).orElse(null);
        String writer = thisboard.getWriter();
        if (u == null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        if (!authId.equals(writer)) {

            if( u.isAdmin() == false) {
                throw new InsufficientpermissionException();
            }//이글의 writer와 로그인된 유저 authId가 다른 경우
        }

        InquiryRepository.deleteById(id);

    }//게시글 삭제


    @RequestMapping(value = "/inquiry/{id}", method = RequestMethod.PUT)
    public Inquiry editBoard(@RequestBody ObjectNode saveObj, @PathVariable String id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Inquiry newboard = mapper.treeToValue(saveObj.get("inquiry"), Inquiry.class);
        String authId = saveObj.get("authenticationId").asText();
        Inquiry lastboard = InquiryRepository.findById(id).orElse(null);
        User u = userRepository.findById(authId).orElse(null);
        if (u == null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        if (lastboard==null || !authId.equals(lastboard.getWriter())) {
            if( u.isAdmin() == false){
                throw new InsufficientpermissionException();
            }
            //이글의 writer와 로그인된 유저 authId가 다른 경우
        }
        return InquiryRepository.findById(id)
                .map(inquiry -> {
                    inquiry.setContents(newboard.getContents());
                    inquiry.setTitle(newboard.getTitle());
                    return InquiryRepository.save(inquiry);
                })
                .orElseGet(() -> {
                    newboard.setId(id);
                    return InquiryRepository.save(newboard);
                });
    }//게시글 수정하기

    @RequestMapping(value = "/inquiry/change/{id}", method = RequestMethod.PUT)
    public Inquiry solved(@RequestBody ObjectNode saveObj, @PathVariable String id) {
        String authId = saveObj.get("authenticationId").asText();
        User u = userRepository.findById(authId).orElse(null);
        if (u == null) {
            // 로그인 여부 확인
            throw new WrongAuthenticationIdException();
        }
        if (u.isAdmin() != true) {
            throw new InsufficientpermissionException();
        }
        return InquiryRepository.findById(id)
                .map(inquiry -> {
                    inquiry.setConfirmed(true);
                    return InquiryRepository.save(inquiry);
                })
                .orElseGet(() -> {
                    throw new InvalidInquiryException();
                });//요구사항 해결 완료
    }

    @ExceptionHandler(InvalidInquiryException.class)
    public InquiryError InvalidInquiryExceptionHandler(InvalidInquiryException e) {
        return new InquiryError("There is no board.");
    }

    @ExceptionHandler(WrongAuthenticationIdException.class)
    public InquiryError WrongAuthenticationIdExceptionHandler(WrongAuthenticationIdException e) {
        return new InquiryError("AuthenticationId is wrong.");
    }

    @ExceptionHandler(InsufficientpermissionException.class)
    public InquiryError InsufficientpermissionExceptionHandler(InsufficientpermissionException e) {

        return new InquiryError("Permission is not sufficient.");
    }

    @Generated
    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Inquiry data = new Inquiry();
            data.setId(nextLine[0]);
            data.setTitle(nextLine[1]);
            data.setContents(nextLine[2]);
            data.setWriter(nextLine[3]);
            data.setConfirmed(Boolean.parseBoolean(nextLine[4]));
            InquiryRepository.save(data);
        }
    }
}