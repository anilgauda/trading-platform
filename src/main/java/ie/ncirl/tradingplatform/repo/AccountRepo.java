package ie.ncirl.tradingplatform.repo;

import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByNumber(String number);
}