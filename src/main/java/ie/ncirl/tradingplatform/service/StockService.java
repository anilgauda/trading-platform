package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.Stock;
import ie.ncirl.tradingplatform.model.StockTransaction;
import ie.ncirl.tradingplatform.repo.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepo stockRepo;

    public int findQuantityInStockByAccountAndSymbol(Account account, String symbol) {
        Optional<Stock> stock = stockRepo.findByAccountAndSymbol(account, symbol);
        int quantity = 0;
        if (stock.isPresent()) {
            for (StockTransaction stockTransaction : stock.get().getStockTransactions()) {
                quantity += stockTransaction.getSellPrice() == null ?
                        stockTransaction.getQuantity() : -stockTransaction.getQuantity();
            }
        }
        return quantity;
    }
}
