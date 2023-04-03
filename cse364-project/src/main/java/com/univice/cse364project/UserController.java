package com.univice.cse364project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID: {}.", userId);
        return userRepository.findById(userId).orElse(null);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user) {
        LOG.info("Saving user.");
        return userRepository.save(user);
    }

}
