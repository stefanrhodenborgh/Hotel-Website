package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	Optional<Account> findByToken(String token);

}
