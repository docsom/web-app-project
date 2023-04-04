package com.univice.cse364project.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class RatingController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @RequestMapping(value = "/ratings", method = RequestMethod.GET)
    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }
    @RequestMapping(value = "/rating/create", method = RequestMethod.POST)
    public Rating addNewRating(@RequestBody Rating rating){
        return ratingRepository.save(rating);
    }

}
