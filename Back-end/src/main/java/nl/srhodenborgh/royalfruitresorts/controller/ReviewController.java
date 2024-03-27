package nl.srhodenborgh.royalfruitresorts.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


    // Create
    @PostMapping("/create-review")
    public boolean createReview(@RequestParam long hotelId, @RequestBody Review review, HttpServletRequest request) {
    	Account account = (Account) request.getAttribute("RFR_ACC");
    	if (account == null) {
            logger.error("Failed to create review. Cannot find account");
    		return false;
    	}

        return reviewService.createReview(hotelId, account, review);
    }



    // Read
    @GetMapping("/all-reviews")
    public Iterable<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }


    @GetMapping("/review/{id}")
    public Optional<Review> getReview(@PathVariable("id") long id) {
        return reviewService.getReview(id);
    }



    // Update
    @PutMapping("/update-review/{id}")
    public boolean updateReview(@PathVariable ("id") long id, @RequestBody Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }



    // Delete
    @DeleteMapping("/delete-review/{id}")
    public boolean deleteReview(@PathVariable ("id") long id) {
        return reviewService.deleteReview(id);
    }
}
