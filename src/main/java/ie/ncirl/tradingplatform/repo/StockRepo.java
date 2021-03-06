package ie.ncirl.tradingplatform.repo;

import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {
    Optional<Stock> findByAccountAndSymbol(Account account, String symbol);

    List<Stock> findAllByAccount(Account account);
}