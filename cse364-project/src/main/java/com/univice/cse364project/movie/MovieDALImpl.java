package com.univice.cse364project.movie;

import com.univice.cse364project.rating.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public Movie getMovieById(Long Id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(Id));
        return mongoTemplate.findOne(query, Movie.class);
    }
    @Override
    public Movie editMovie(Movie newMovie, Long id){
        Movie m = mongoTemplate.findById(id, Movie.class);
        if( m != null){
            m.setTitle(newMovie.getTitle());
            m.setGenre(newMovie.getGenre());
            return mongoTemplate.save(m);
        }else{
            newMovie.setMovieId(id);
            return mongoTemplate.save(newMovie);
        }
    }

    @Override
    public Movie addNewMovie(Movie movie){
        mongoTemplate.save(movie);

        return movie;
    }
}
