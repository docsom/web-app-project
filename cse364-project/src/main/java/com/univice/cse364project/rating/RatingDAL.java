package com.univice.cse364project.rating;

import java.util.List;

public interface RatingDAL {

    List<Rating> getAllRatings();

    Rating addNewRating(Rating rating);
}
