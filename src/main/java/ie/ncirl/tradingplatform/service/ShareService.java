package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.StockTransaction;
import ie.ncirl.tradingplatform.repo.StockRepo;
import ie.ncirl.tradingplatform.vo.MyStockVo;
import ie.ncirl.tradingplatform.vo.SharesTableVo;
import ie.ncirl.tradingplatform.vo.TransactionHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShareService {

    @Autowired
    StockRepo stockRepo;

    public Stock getShareDetails(String name) throws IOException {
        Stock stock = YahooFinance.get(name, true);
        return stock;
    }

    public List<Stock> getShareDetails(List<String> name) throws IOException {
        List<Stock> stocksList = new ArrayList<>();
        for (String stocks : name) {
            Stock stock = YahooFinance.get(stocks, true);
            stocksList.add(stock);
        }
        return stocksList;
    }

    public List<SharesTableVo> getAllShares() {
        List<SharesTableVo> sharesList = new ArrayList<>();
        Resource resource = new ClassPathResource("stocks.txt");
        ArrayList<String> stocks = new ArrayList<>();
        try {
            InputStream inputStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String tickerName = null;
            while ((tickerName = br.readLine()) != null) {
                stocks.add(tickerName.substring(0, tickerName.indexOf('|')));
            }

            for (String symbol : stocks) {
                Stock stock = YahooFinance.get(symbol, true);
                sharesTOSharesTableVo(stock, sharesList);
            }

        } catch (IOException e) {
            log.error("Error Occurred while fetching data", e);
        }
        return sharesList;
    }

    public void sharesTOSharesTableVo(Stock stock, List<SharesTableVo> sharesList) {
        SharesTableVo shareTableVo = new SharesTableVo();
        shareTableVo.setName(stock.getName());
        shareTableVo.setSymbol(stock.getSymbol());
        shareTableVo.setPrice(stock.getQuote().getPrice().doubleValue());
        shareTableVo.setOpen(stock.getQuote().getOpen().doubleValue());
        shareTableVo.setPrevClose(stock.getQuote().getPreviousClose().doubleValue());
        shareTableVo.setVol(stock.getQuote().getVolume().doubleValue());
        shareTableVo.setEps(stock.getStats().getEps() == null ? 0 : stock.getStats().getEps().doubleValue());
        shareTableVo.setPe(stock.getStats().getPe() == null ? 0 : stock.getStats().getPe().doubleValue());
        shareTableVo.setBookVal(stock.getStats().getBookValuePerShare() == null ? 0 : stock.getStats().getBookValuePerShare().doubleValue());
        shareTableVo.setMcap(stock.getStats().getMarketCap() == null ? 0 : stock.getStats().getMarketCap().doubleValue());
        sharesList.add(shareTableVo);
    }

    public List<MyStockVo> getSharesForAccount(Account account) throws IOException {
        List<ie.ncirl.tradingplatform.model.Stock> stocks = stockRepo.findAllByAccount(account);

        return stocks.stream().map(stock -> {
            try {
                Stock yahooStock = YahooFinance.get(stock.getSymbol(), true);
                double totalBuyPrice = 0d;
                double totalSellPrice = 0d;
                int numBuyPrice = 0;
                int numSellPrice = 0;
                int quantity = 0;
                for (StockTransaction stockTransaction : stock.getStockTransactions()) {
                    if (stockTransaction.getBuyPrice() != null) {
                        numBuyPrice++;
                        totalBuyPrice += stockTransaction.getBuyPrice();
                        quantity += stockTransaction.getQuantity();
                    }

                    if (stockTransaction.getSellPrice() != null) {
                        numSellPrice++;
                        totalSellPrice += stockTransaction.getSellPrice();
                        quantity -= stockTransaction.getQuantity();
                    }
                }

                // Avoid 0/0 division NaN issue
                double avgBuyPrice = numBuyPrice == 0 ? 0 : totalBuyPrice / numBuyPrice;
                double avgSellPrice = numSellPrice == 0 ? 0 : totalSellPrice / numSellPrice;

                return MyStockVo.builder()
                        .name(yahooStock.getName())
                        .symbol(yahooStock.getSymbol())
                        .currPrice(yahooStock.getQuote().getPrice().doubleValue())
                        .open(yahooStock.getQuote().getOpen().doubleValue())
                        .myAvgBuyPrice(Math.round(avgBuyPrice * 100.0) / 100.0)
                        .myAvgSellPrice(Math.round(avgSellPrice * 100.0) / 100.0)
                        .quantity(quantity)
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).filter(myStockVo -> myStockVo.getQuantity() > 0).collect(Collectors.toList());
    }

    public List<TransactionHistoryVo> getTransactionsForAccount(Account activeAccount) {
        Map<String, String> symbolNameMap = new HashMap<>();
        return stockRepo.findAllByAccount(activeAccount).stream()
                .flatMap(stock -> stock.getStockTransactions().stream())
                .map(stockTransaction -> {
                            String symbol = stockTransaction.getStock().getSymbol();
                            return TransactionHistoryVo.builder()
                                    .name(getNameFromSymbol(symbol, symbolNameMap))
                                    .symbol(symbol)
                                    .type(stockTransaction.getBuyPrice() == null ? "SELL" : "BUY")
                                    .price(stockTransaction.getBuyPrice() == null ?
                                            stockTransaction.getSellPrice() : stockTransaction.getBuyPrice())
                                    .quantity(stockTransaction.getQuantity())
                                    .tradedOn(stockTransaction.getCreated())
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    /**
     * Gets name from Yahoo api and caches it in map so that it doesnt call external api every time
     *
     * @param symbol        stock company symbol
     * @param symbolNameMap company symbol name map
     * @return company name
     */
    public String getNameFromSymbol(String symbol, Map<String, String> symbolNameMap) {
        String name = "";
        if (symbolNameMap.containsKey(symbol)) {
            name = symbolNameMap.get(symbol);
        } else {
            try {
                name = YahooFinance.get(symbol, false).getName();
            } catch (IOException e) {
                log.error("Unable to get symbol data from Yahoo finance API");
            }
            symbolNameMap.put(symbol, name);
        }
        return name;
    }
}
