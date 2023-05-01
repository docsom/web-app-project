package com.univice.cse364project.inquiry;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

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
