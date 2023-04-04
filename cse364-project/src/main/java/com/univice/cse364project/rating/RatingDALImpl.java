package com.univice.cse364project.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RatingDALImpl implements RatingDAL {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Rating> getAllRatings() {
        return mongoTemplate.findAll(Rating.class);
    }

    @Override
    public Rating addNewRating(Rating rating){
        mongoTemplate.save(rating);

        return rating;
    }
}
