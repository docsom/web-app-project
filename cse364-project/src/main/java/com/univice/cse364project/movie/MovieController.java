package com.univice.cse364project.movie;

import com.univice.cse364project.rating.Rating;
import com.univice.cse364project.rating.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final MovieRepository MovieRepository;

    private final RatingRepository ratingRepository;

    public MovieController(MovieRepository movieRepository, RatingRepository ratingRepository) {
        this.MovieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
    }
    public  List<Rating> getRatings() {return ratingRepository.findAll();}

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public List<Movie> getAllMovies(){
        return MovieRepository.findAll();
    }
    @RequestMapping(value = "/movie/create", method = RequestMethod.POST)
    public Movie addNewMovie(@RequestBody Movie movie){
        return MovieRepository.save(movie);
    }

    @RequestMapping(value = "/ratings/{ratingValue}", method = RequestMethod.GET)
    public List<Movie> getOverRatingMovies(@PathVariable int ratingValue){
        List<Movie> total = getAllMovies();
        List<Movie> target = new ArrayList<>();
        List<Rating> ratings = getRatings();

        for(int i = 0; i < total.size(); i++ ){
            Movie m = total.get(i);
            long movieId = m.getMovieId();
            int sum = 0;
            int counter = 0;
            for(int j = 0; j < ratings.size(); j++ ){
                Rating r = ratings.get(j);
                if(movieId == r.getMovieId()){
                    sum += r.getRatingValue();
                    counter++;
                }
            }
            double avg = sum/counter;
            if(avg >= ratingValue){
                target.add(m);
            }
        }
        return target;
    }

}
