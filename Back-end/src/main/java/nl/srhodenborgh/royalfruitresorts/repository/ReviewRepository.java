package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReviewRepository extends CrudRepository<Review, Long> {

}
