package nl.srhodenborgh.royalfruitresorts.repository;

import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	Optional<Account> findByToken(String token);
	
}
