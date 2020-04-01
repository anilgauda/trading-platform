package ie.ncirl.tradingplatform.repo;

import ie.ncirl.tradingplatform.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransactionRepo extends JpaRepository<StockTransaction, Long> {
}