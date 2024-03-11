package nl.srhodenborgh.royalfruitresorts.repository;

import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndAccountIsNotNull(String email);
}
