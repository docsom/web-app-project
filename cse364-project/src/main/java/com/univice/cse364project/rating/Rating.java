package com.univice.cse364project.rating;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rating")
public class Rating {
    @Id
    @JsonIgnore
    private String id;

    private Long userId;

    private Long movieId;

    private int ratingValue;

    private String timeStamp;

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
