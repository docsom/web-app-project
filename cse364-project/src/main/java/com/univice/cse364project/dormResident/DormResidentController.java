package com.univice.cse364project.dormResident;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.Generated;
import com.univice.cse364project.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

@Generated
@RestController
@RequestMapping(value = "/")
public class DormResidentController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final DormResidentRepository dormResidentRepository;

    public DormResidentController(DormResidentRepository dormResidentRepository) throws IOException, CsvException {
        this.dormResidentRepository = dormResidentRepository;
        long size = dormResidentRepository.count();
        if(size == 0){
            LOG.info("Loading dormitory residents");
            readDataFromCsv("dorm-resident.csv");
        }
    }

    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            DormResident data = new DormResident();
            data.setStudentNumber(nextLine[0]);
            data.setIdNumber(nextLine[1]);
            dormResidentRepository.save(data);
        }
    }
}
