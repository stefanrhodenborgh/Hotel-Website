package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndAccountIsNotNull(String email);
}
