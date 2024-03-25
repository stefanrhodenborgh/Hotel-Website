package nl.srhodenborgh.royalfruitresorts.service;

import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Hotel;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.repository.AccountRepository;
import nl.srhodenborgh.royalfruitresorts.repository.HotelRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReviewRepository;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private DataFormatter dataFormatter;
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);



    // Create
    public boolean createReview(long hotelId, Account account, Review review) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to create review. Cannot find hotel (id: {})", hotelId);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(review) || inputValidator.isNameInvalid(review.getName())) {
            logger.error("Failed to create review. Input fields are invalid");
            return false;
        }

        dataFormatter.formatFields(review);
        review.setHotel(hotelOptional.get());
        review.setAccount(account);
        review.setDate(LocalDateTime.now());

    	reviewRepository.save(review);
        logger.info("Successfully created review on Id: {}", review.getId());
        return true;
    }



    // Read
    public Iterable<Review> getAllReviews() {
        Iterable<Review> reviews = reviewRepository.findAll();

        if (!reviews.iterator().hasNext()) {
            logger.error("No reviews found in database");
        }

        return reviews;
    }


    public Optional<Review> getReview(long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isEmpty()) {
            logger.error("Failed to get review. Cannot find review (id: {})", id);
        }

        return reviewOptional;
    }


    // Update
    public boolean updateReview(long id, Review updatedReview) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isEmpty()) {
            logger.error("Failed to update review. Cannot find review (id: {})", id);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedReview)) {
            logger.error("Failed to update review (id: {}). Input fields are invalid", id);
            return false;
        }

        dataFormatter.formatFields(updatedReview);

        Review review = reviewOptional.get();
        review.setComment(updatedReview.getComment());
        review.setRating(updatedReview.getRating());

        reviewRepository.save(review);
        logger.info("Successfully updated review (id: {})", id);
        return true;
    }


    // Delete
    public boolean deleteReview(long id) {
        Optional<Review> reviewOptional = reviewRepository.findById((id));

        if (reviewOptional.isEmpty()) {
            logger.error("Failed to delete review. Cannot find review (id: {})", id);
            return false;
        }

        reviewRepository.deleteById(id);
        logger.info("Successfully deleted review (id: {})", id);
        return true;
    }



    // Andere methodes
}
