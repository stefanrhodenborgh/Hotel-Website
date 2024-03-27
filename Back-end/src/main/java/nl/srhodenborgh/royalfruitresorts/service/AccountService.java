package nl.srhodenborgh.royalfruitresorts.service;

import jakarta.servlet.http.HttpServletRequest;
import nl.srhodenborgh.royalfruitresorts.enums.Role;
import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.model.User;
import nl.srhodenborgh.royalfruitresorts.repository.AccountRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReviewRepository;
import nl.srhodenborgh.royalfruitresorts.repository.UserRepository;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private DataFormatter dataFormatter;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);



    // Create
    public Long createAccount(Account account) {
        // TODO: moet dit long teruggeven? Als er een account wordt gemaakt moet hij meteen een token maken denk ik

        if (inputValidator.areRequiredFieldsInvalid(account)) {
            logger.error("Failed to create account. Input fields are invalid");
            return null;
        }

        if (userRepository.findByEmailAndAccountIsNotNull(account.getUser().getEmail()).isPresent()) {
            logger.error("Account already exists on email: " + account.getUser().getEmail());
            return null;
        }

        dataFormatter.formatFields(account.getUser());

        account.getUser().setEmail(account.getUser().getEmail().toLowerCase());
        account.setRole(Role.GUEST);
        account.setLoyaltyPoints(settingsService.getLoyaltyPointsStartAmount());
        account.setHotelId(Account.USER_HOTEL_ID);
        account.setUser(account.getUser());

        userRepository.save(account.getUser());
        accountRepository.save(account);

        logger.info("Successfully created account on Id: {}", account.getId());
        return account.getUser().getId();
    }



    // Read
    public Iterable<Account> getAllAccounts() {
        Iterable<Account> accounts = accountRepository.findAll();

        if (!accounts.iterator().hasNext()) {
            logger.error("No accounts found in database");
        }

        return accounts;
    }


    public Optional<Account> getAccount(long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isEmpty()) {
            logger.error("Failed to get account. Cannot find account (id: {})", id);
        }

        return accountOptional;
    }


    public Optional<Account> getAccountFromToken(HttpServletRequest request) {
        Account account = (Account)request.getAttribute("RFR_ACC");
        return Optional.ofNullable(account);
    }


    public Iterable<Review> getReviewsFromAccount(long id) {

        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isEmpty()) {
            logger.error("Failed to get reviews. Cannot find account (id: {})", id);
            return null;
        }

        List<Review> reviews = accountOptional.get().getReviews();

        if (reviews.isEmpty()) {
            logger.warn("No reviews found on account (id: {})", id);
        }

        return reviews;
    }



    // Update
    public boolean changePassword(Account account, String newPassword) {

        if (inputValidator.isPasswordInvalid(newPassword)) {
            logger.error("Failed to change password of account (id: {}). Password is invalid", account.getId());
        }

        account.setPassword(newPassword);
        accountRepository.save(account);
        logger.info("Password successfully changed od account (id: {})", account.getId());
        return true;
    }


    // Delete
    public boolean deleteAccount(long id) {
    	Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isEmpty()) {
            logger.error("Failed to delete account. Cannot find account (id: {})", id);
            return false;
        }

        Account account = accountOptional.get();

        User user = account.getUser();
        user.setAccount(null);
        userRepository.save(user);

        Iterable<Review> reviews = account.getReviews();
        for (Review review : reviews) {
            review.setAccount(null);
            reviewRepository.save(review);
        }

        accountRepository.deleteById(account.getId());
        logger.info("Successfully deleted account (id: {})", id);
        return true;
    }



    // Andere methodes
    public Account login(String email, String password) {

    	Optional<User> userOptional = userRepository.findByEmailAndAccountIsNotNull(email);
    	if (userOptional.isEmpty()) {
            logger.error("Failed to login. Cannot find user (email: {})", email);
    		return null;
    	}

    	User dbUser = userOptional.get();
        // TODO: Moet dit ignore case zijn?
    	if (!dbUser.getAccount().getPassword().equalsIgnoreCase(password)) {
            logger.error("Failed to login. Password is invalid");
    		return null;
    	}
    	
    	// Generate token
    	int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 100;
        Random random = new Random();

    	String generatedToken = random.ints(leftLimit, rightLimit + 1)
    		      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
    		      .limit(targetStringLength)
    		      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    		      .toString();

    	dbUser.getAccount().setToken(generatedToken);
    	accountRepository.save(dbUser.getAccount());
    	
    	return dbUser.getAccount();
    }


    public boolean isEmailAvailable(String email) {
        Optional<User> userOptional = userRepository.findByEmailAndAccountIsNotNull(email);

        if (userOptional.isPresent()) {
            logger.error("This email address is not available");
            return false;
        }

        return true;
    }
}
