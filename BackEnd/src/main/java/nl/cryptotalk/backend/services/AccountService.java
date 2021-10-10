package nl.cryptotalk.backend.services;

import nl.cryptotalk.backend.Repositories.AccountRepository;
import nl.cryptotalk.backend.entities.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService (AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account saveAccount(Account account) {
        return this.accountRepository.save(account);
    }

    public List<Account> findAllAccounts() {
        return this.accountRepository.findAll();
    }

    public Account getOneAccount(String username) {
        return this.accountRepository.findByUsername(username);
    }

    public Optional<Account> getOneAccount(Long id) {
        return this.accountRepository.findById(id);
    }

    public boolean accountExists(Long id){
        return this.accountRepository.existsById(id);
    }

    public boolean accountExists(String username){
        return this.accountRepository.existsByUsername(username);
    }

    public void deleteAccount(Long id) {
        this.accountRepository.deleteById(id);
    }

    public List<Account> getNewestAccounts(int amount){
        return this.accountRepository.getNewestAccounts(amount);
    }

}
