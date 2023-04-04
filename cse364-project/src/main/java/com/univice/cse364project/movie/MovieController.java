package com.univice.cse364project.movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final MovieRepository MovieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.MovieRepository = movieRepository;
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public List<Movie> getAllMovies(){
        return MovieRepository.findAll();
    }
    @RequestMapping(value = "/movie/create", method = RequestMethod.POST)
    public Movie addNewMovie(@RequestBody Movie movie){
        return MovieRepository.save(movie);
    }

//    @RequestMapping(value = "/ratings/{ratingValue}", method = RequestMethod.GET)
//    public List<Movie> getOverRatingMovies(@PathVariable int ratingValue){
//
//    }

}
