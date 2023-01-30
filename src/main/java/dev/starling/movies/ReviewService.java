package dev.starling.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    //this mongoTemplate is used to update the movie object without having to use the movieRepository
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
       Review review = reviewRepository.insert(new Review(reviewBody, LocalDateTime.now(),LocalDateTime.now()));



       mongoTemplate.update(Movie.class)
               .matching(Criteria.where("imdbId").is(imdbId))
               .apply(new Update().push("reviewIds").value(review))
               .first();

       return review;

    }
}
