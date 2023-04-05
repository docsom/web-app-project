package com.univice.cse364project.movie;

import com.univice.cse364project.rating.Rating;
import com.univice.cse364project.user.User;

import java.util.List;

public interface MovieDAL {

    List<Rating> getRatings();

    List<Movie> getAllMovies();

    Movie editNewMovie(Movie newMovie, Long id);

    Movie addNewMovie(Movie movie);
}
