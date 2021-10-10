package nl.cryptotalk.backend.controllers;

import nl.cryptotalk.backend.entities.Account;
import nl.cryptotalk.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Get a list of all accounts that are in the database
     * @return returns a list of accounts, this list can be empty if there are none
     */
    @GetMapping
    public List<Account> findAllAccounts(){
        return this.accountService.findAllAccounts();
    }

    /**
     * get a list of the {amount} newest accounts
     * @param amount maximum amount of accounts to return
     * @return returns a list of x amount of newest accounts, list can be empty
     */
    @GetMapping("/newest")
    public List<Account> findNewestAccounts(@RequestParam int amount){
        return this.accountService.getNewestAccounts(amount);
    }

    /**
     * Find a user account based on the username
     * @param username the username of the account to search
     * @return returns the account with this username if it exists, or throws an error if it does not
     */
    @GetMapping("/search")
    public Account findAccountByUsername(@RequestParam String username){
        Account account = this.accountService.getOneAccount(username);
        if(account == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This username does not exist yet!");
        }
        else{
            return account;
        }
    }

    /**
     * Get an account based on an account id
     * @param id the id of the account to get
     * @return returns the account with this id, or throws an error if it does not exists
     */
    @GetMapping("/{id}")
    public Account findAccountById(@PathVariable Long id){
        Optional<Account> account = this.accountService.getOneAccount(id);
        if(account.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with id " + id + "!");
        }
        else{
            return account.get();
        }
    }

    /**
     * Add a new account to the database
     * @param account the account to add
     * @return returns the account if creating was successfull
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account addNewAccount(@RequestBody Account account){
        if(account.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please do not provide an id");
        }
        if(this.accountService.accountExists(account.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username already exists");
        }
        return this.accountService.saveAccount(account);
    }

    /**
     * Update the data of an account
     * @param id id of the account to update
     * @param account updated account
     * @return returns the updated account
     */
    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account account){
        if(id != account.getId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id of the given account does not match the path id.");
        }
        if(!this.accountService.accountExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account with this id has been found to update!");
        }
        return this.accountService.saveAccount(account);
    }

    /**
     * Delete an account from the database
     * @param id id of the account to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id){
        if(!this.accountService.accountExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account with id " + id + " has been found in the database.");
        }
        this.accountService.deleteAccount(id);
    }
}
