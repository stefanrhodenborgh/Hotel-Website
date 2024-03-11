package nl.srhodenborgh.royalfruitresorts.controller;

import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.dto.AccountDTO;
import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class AccountController {
    @Autowired
    private AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);



    // Create
    @PostMapping("/create-account")
    public Long createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }



    // Read
    @GetMapping("/all-accounts")
    public Iterable<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    public Optional<Account> getAccount(@PathVariable("id") long accountId) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/get-account")
    public Optional<Account> getAccountFromToken(HttpServletRequest request) {
        return accountService.getAccountFromToken(request);
    }

    @GetMapping("/account/{id}/reviews")
    public Iterable<Review> getReviewsFromAccount(@PathVariable ("id") long id) {
        return accountService.getReviewsFromAccount(id);
    }




    // Update
    @PutMapping("/account/change-password")
    public boolean changePassword(@RequestBody String newPassword, HttpServletRequest request) {
        Account account = (Account) request.getAttribute("YC_ACCOUNT");

        if (account == null) {
            logger.error("Failed to change password. Token is invalid");
            return false;
        }

        return accountService.changePassword(account, newPassword);
    }


    // Delete
    @DeleteMapping("/delete-account/{id}")
    public boolean deleteAccount(@PathVariable ("id") long id) {
        return accountService.deleteAccount(id);
    }



    // Andere methodes
    @PostMapping("/login")
    public Account login(@RequestBody AccountDTO accountDTO) {
        return accountService.login(accountDTO.getEmail(), accountDTO.getPassword());
    }

    @PostMapping("is-email-available")
    public boolean isEmailAvailable(@RequestBody String email) {
        return accountService.isEmailAvailable(email);
    }

}

