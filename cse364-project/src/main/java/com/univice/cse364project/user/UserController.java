package com.univice.cse364project.user;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository UserRepository;

    public UserController(UserRepository UserRepository) throws IOException, CsvException {
        this.UserRepository = UserRepository;
        long size = UserRepository.count();
        if(size == 0){
            LOG.info("Loading users");
            readDataFromCsv("users.csv");
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return UserRepository.findAll();
    }
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long userId) {
        LOG.info("Getting user with ID: {}.", userId);
        return UserRepository.findById(userId).orElse(null);
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user) {
        LOG.info("Saving user.");
        return UserRepository.save(user);
    }

    @PutMapping("user/{id}")
    public User editUser(@RequestBody User newUser, @PathVariable Long id){

        return UserRepository.findById(id)
                .map(user -> {
                    user.setAge(newUser.getAge());
                    user.setGender(newUser.getGender());
                    user.setOccupation(newUser.getOccupation());
                    user.setZipcode(newUser.getZipcode());
                    return UserRepository.save(user);
                })
                .orElseGet(()->{
                    newUser.setUserId(id);
                    return UserRepository.save(newUser);
                });
    }

    public void readDataFromCsv(String fileName) throws IOException, CsvException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new FileReader(classLoader.getResource(fileName).getPath()));
        List<String[]> rows = reader.readAll();
        for (String[] row : rows) {
            User data = new User();
            data.setUserId(Long.parseLong(row[0]));
            data.setGender(row[1]);
            data.setAge(Integer.parseInt(row[2]));
            data.setOccupation(Integer.parseInt(row[3]));
            data.setZipcode(row[4]);
            UserRepository.save(data);
        }
    }
}
