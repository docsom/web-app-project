package com.univice.cse364project.user;

import com.univice.cse364project.movie.Movie;
import com.univice.cse364project.movie.MovieRepository;
import com.univice.cse364project.user.User;
import com.univice.cse364project.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository UserRepository;

    public UserController(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
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
}
