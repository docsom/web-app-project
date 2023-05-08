package com.univice.cse364project.inquiry;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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



import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping(value="/")
public class InquiryController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final InquiryRepository InquiryRepository;

    public InquiryController(InquiryRepository inquiryRepository) throws IOException, CsvException {
        this.InquiryRepository = inquiryRepository;
        long size = inquiryRepository.count();
        if(size == 0) {
            LOG.info("Loading inquiries");
            readDataFromCsv("inquiry.csv");
        }
    }


    @RequestMapping(value="/inquirys", method=RequestMethod.GET)
    public List<Inquiry> getAllInquiry(){
        return InquiryRepository.findAll();
    }//게시글 보여주기

    @RequestMapping(value="/inquiry/{id}", method=RequestMethod.GET)
    public Inquiry getBoard(@PathVariable String id){
        LOG.info("Getting Board with Id: {}.", id);
        return InquiryRepository.findById(id).orElse(null);
    }//게시글 찾기


    @RequestMapping(value="/inquiry/write", method=RequestMethod.POST)
    public Inquiry insertBoard(@RequestBody Inquiry inquiry){
        return InquiryRepository.save(inquiry);
    }//게시글 작성


    @RequestMapping(value="/inquiry/{id}", method=RequestMethod.DELETE)
    public void deleteBoard(@PathVariable String id){
        InquiryRepository.deleteById(id);
    }//게시글 삭제

    @RequestMapping(value="/inquiry/{id}", method=RequestMethod.PUT)
    public Inquiry editboard(@RequestBody Inquiry newboard, @PathVariable String id) {
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


    @RequestMapping(value="/inquiry/change/{id}", method=RequestMethod.PUT)
         public Inquiry Solved (@PathVariable String id){
             return InquiryRepository.findById(id)
                     .map(inquiry -> {
                         inquiry.setConfirmed(true);
                         return InquiryRepository.save(inquiry);
                     })
                     .orElseGet(null);//요구사항 해결 완료
         }
    /*
    @RequestMapping(value="/inquiry/write", method=RequestMethod.GET)
    public String openBoardWrite() throws Exception {
        return "/inquiry/restBoardWrite";
    }//게시글 작성 화면인데 보류
*/



    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
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
