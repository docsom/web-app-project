package com.univice.cse364project.movie;

import com.univice.cse364project.rating.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDALImpl implements MovieDAL {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Rating> getRatings() {return mongoTemplate.findAll(Rating.class);}
    @Override
    public List<Movie> getAllMovies() { return mongoTemplate.findAll(Movie.class);}

    @Override
    public Movie addNewMovie(Movie movie){
        mongoTemplate.save(movie);

        return movie;
    }
}
