package com.univice.cse364project.device;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
@RequestMapping(value = "/")
class DeviceController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) throws IOException, CsvException {
        this.deviceRepository = deviceRepository;
        long size = deviceRepository.count();
        if(size == 0) {
            LOG.info("Loading devices");
            readDataFromCsv("device.csv");
        }
    }
    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Device data = new Device();
            data.setId(nextLine[0]);
            data.setName(nextLine[1]);
            data.setType(nextLine[2]);
            if(!Objects.equals(nextLine[5], "")) {
                LocalDate startDate = LocalDate.parse(nextLine[3], DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDate endDate = LocalDate.parse(nextLine[4], DateTimeFormatter.ofPattern("yyyyMMdd"));
                data.setStartDate(startDate);
                data.setEndDate(endDate);
            }
            deviceRepository.save(data);
        }
    }
}
