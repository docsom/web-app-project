package com.univice.cse364project.user;

import com.univice.cse364project.movie.Movie;
import com.univice.cse364project.user.User;

import java.util.List;

public interface UserDAL {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User addNewUser(User user);

    User editUser(User newUser, Long id);
}
