package com.univice.cse364project.user;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping(value = "/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) throws IOException, CsvException {
        this.userRepository = userRepository;
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
}
