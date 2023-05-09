package com.univice.cse364project.device;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.univice.cse364project.user.User;
import com.univice.cse364project.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/")
public class DeviceController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceController(DeviceRepository deviceRepository, UserRepository userRepository) throws IOException, CsvException {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        long size = deviceRepository.count();
        if(size == 0) {
            LOG.info("Loading devices");
            readDataFromCsv("device.csv");
        }
    }
    @RequestMapping(value="/devices", method = RequestMethod.GET)
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    @RequestMapping(value = "/devices/{type}", method = RequestMethod.GET)
    public List<Device> getAllTypeDevices(@PathVariable String type) {
        List<Device> total = getAllDevices();
        List<Device> target = new ArrayList<>();
        for (Device d : total) {
            if (d.getType().equals(type)) {
                target.add(d);
            }
        }
        return target;
    }
    @PutMapping(value="/device/{id}")
    public Device rentDevice(@RequestParam String user, @PathVariable String id) {
        User u = userRepository.findById(user).orElse(null);
        return deviceRepository.findById(id)
                .map(device -> {
                    device.setStartDate(LocalDate.now());
                    device.setEndDate(LocalDate.now().plusMonths(3));
                    device.setCurrentUser(u);
                    return deviceRepository.save(device);
                })
                .orElseGet(() -> {
                    return null;
                });
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
