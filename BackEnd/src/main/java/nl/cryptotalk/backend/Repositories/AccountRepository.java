package nl.cryptotalk.backend.Repositories;

import nl.cryptotalk.backend.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    /**
     * get a list of all accounts
     * @return list of accounts
     */
    @Override
    List<Account> findAll();

    /**
     * find an account by username
     * @param username
     * @return
     */
    Account findByUsername(String username);

    /**
     * check if this username exists
     * @param username username to check
     * @return returns true if the username exists
     */
    boolean existsByUsername(String username);

    /**
     * get a list of the newest accounts, ordered by createdOn, does not include accounts where createdOn is null
     * @param amount max amount of accounts to get
     * @return returns list of accounts, can be null
     */
    @Query(value = "select * from Account a where a.created_on is not null order by a.created_on desc LIMIT :amount", nativeQuery = true)
    List<Account> getNewestAccounts(@Param("amount") int amount);
}
