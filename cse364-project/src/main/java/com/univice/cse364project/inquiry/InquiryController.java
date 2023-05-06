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

import java.io.FileReader;
import java.io.IOException;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;

@RestController
@RequestMapping(value="/")
public class InquiryController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final InquiryRepository inquiryRepository;

    public InquiryController(InquiryRepository inquiryRepository) throws IOException, CsvException {
        this.inquiryRepository = inquiryRepository;
        long size = inquiryRepository.count();
        if(size == 0) {
            LOG.info("Loading inquiries");
            readDataFromCsv("inquiry.csv");
        }
    }

    @RequestMapping(value="/inquiry", method=RequestMethod.GET){
    public List<Inquiry> getAllInquiry(){
        return InquiryRepository.findAll();
    }
    }//보드보여주기


    /*
    @RequestMapping(value="/inquiry/write", method=RequestMethod.GET)
    public String openBoardWrite() throws Exception {
        return "/inquiry/restBoardWrite";
    }//게시글 작성 화면인데 보류
*/

     @RequestMapping(value="/inquiry/write", method=RequestMethod.POST){
        public Inquiry insertBoard(@RequestBody Inquiry inquiry){
        return InquiryRepository.save(inquiry);
        }//게시글 작성
        }

     @RequestMapping(value="/inquiry/change/{id}", method=RequestMethod.PUT) {
         public void Solved () {
             return InquiryRepository.findById(id)
                     .map(inquiry -> {
                         inquiry.setConfirmed(true);
                         return InquiryRepository.save(inquiry);
                     })
                     .orElseGet(() -> {
                         LOG.info("It doesn't exist.");
                         return null;
                     });//요구사항 해결 완료(구조 물어보기)
         }
     }

    @RequestMapping(value="/inquiry/{writer}", method=RequestMethod.GET)
    public String getBoard(@PathVariable stirng writer){
        LOG.info("Getting Board with writer: {}.", writer;
        return InquiryRepository.findById(writer).orElse(null);
    }//작성자 게시글 찾기

    @RequestMapping(value="/inquiry/{id}", method=RequestMethod.PUT)
    public Inquiry editboard(@RequestBody Inquiry newboard, @PathVariable Long id) {
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

    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Inquiry data = new Inquiry();
            data.setId(nextLine[0]);
            data.setTitle(nextLine[1]);
            data.setContents(nextLine[2]);
            data.setConfirmed(Boolean.parseBoolean(nextLine[4]));
            inquiryRepository.save(data);
        }
    }
}
