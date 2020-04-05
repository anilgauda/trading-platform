package ie.ncirl.tradingplatform.controller;

import ie.ncirl.tradingplatform.dto.ResponseDTO;
import ie.ncirl.tradingplatform.dto.TradeShareRequest;
import ie.ncirl.tradingplatform.dto.sqs.StockTransactionDTO;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.service.SQSClientService;
import ie.ncirl.tradingplatform.service.ShareService;
import ie.ncirl.tradingplatform.service.StockService;
import ie.ncirl.tradingplatform.service.UserService;
import ie.ncirl.tradingplatform.util.UserUtil;
import ie.ncirl.tradingplatform.vo.MyStockVo;
import ie.ncirl.tradingplatform.vo.SharesTableVo;
import ie.ncirl.tradingplatform.vo.SharesVo;
import ie.ncirl.tradingplatform.vo.TransactionHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    StockService stockService;

    @Autowired
    UserService userService;

    @Autowired
    private SQSClientService sqsClientService;


    @GetMapping("/trade/sharedetails")
    public SharesVo getStocks(@RequestParam(value = "user") String user, @RequestParam(value = "name") String name) throws IOException {
        log.info("user and name from request {},{}", user, name);
        Stock shares = shareService.getShareDetails(name);
        SharesVo sharesVo = new SharesVo();
        sharesVo.setName(shares.getName());
        ArrayList<Double> close = new ArrayList<>();
        ArrayList<Double> open = new ArrayList<>();
        ArrayList<Double> high = new ArrayList<>();
        ArrayList<Double> low = new ArrayList<>();
        ArrayList<Double> volume = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yy");
        for (HistoricalQuote histStock : shares.getHistory()) {
            close.add(histStock.getClose() == null ? 0 : histStock.getClose().doubleValue());
            open.add(histStock.getOpen() == null ? 0 : histStock.getOpen().doubleValue());
            high.add(histStock.getHigh() == null ? 0 : histStock.getHigh().doubleValue());
            low.add(histStock.getLow() == null ? 0 : histStock.getLow().doubleValue());
            volume.add(histStock.getVolume() == null ? 0 : histStock.getVolume().doubleValue());
            Date date = histStock.getDate().getTime();
            dates.add(format1.format(date));
        }
        sharesVo.setClose(close);
        sharesVo.setOpen(open);
        sharesVo.setHigh(high);
        sharesVo.setLow(low);
        sharesVo.setVolume(volume);
        sharesVo.setDate(dates);
        return sharesVo;
    }

    @GetMapping("/trade/all")
    public List<SharesTableVo> getAllStocks() throws IOException {
        return shareService.getAllShares();
    }

    @GetMapping("/trade/my")
    public List<MyStockVo> getMyStocks() throws IOException {
        return shareService.getSharesForAccount(userService.getActiveAccount());
    }

    @GetMapping("/trade/history")
    public List<TransactionHistoryVo> getMyTransactions() throws IOException {
        return shareService.getTransactionsForAccount(userService.getActiveAccount());
    }


    @PostMapping("/trade/share/buy")
    public ResponseDTO<String> buyStock(TradeShareRequest tradeShareRequest) {
        Account account = userService.getActiveAccount();
        if (account.getBalance() >= (tradeShareRequest.getPrice() * tradeShareRequest.getQuantity())) {
            sqsClientService.send(createStockTransactionDTO(tradeShareRequest, true));
            return new ResponseDTO<String>().withData("OK");
        } else {
            return new ResponseDTO<String>().withError("Not enough balance to buy shares. Please top up in My account");
        }
    }

    @PostMapping("/trade/share/sell")
    public ResponseDTO<String> sellStock(TradeShareRequest tradeShareRequest) {
        Account account = userService.getActiveAccount();
        int currQuantity = stockService.findQuantityInStockByAccountAndSymbol(account, tradeShareRequest.getSymbol());
        if (currQuantity >= tradeShareRequest.getQuantity()) {
            sqsClientService.send(createStockTransactionDTO(tradeShareRequest, false));
            return new ResponseDTO<String>().withData("OK");
        } else {
            return new ResponseDTO<String>().withError("Not enough stock quantity available to sell");
        }
    }

    private StockTransactionDTO createStockTransactionDTO(TradeShareRequest tradeShareRequest, boolean isBuy) {
        User user = userService.findByUsername(UserUtil.getCurrentUser().getUsername()).get();
        StockTransactionDTO stockTransactionDTO = StockTransactionDTO.builder()
                .accountId(user.getAccounts().get(0).getId())
                .symbol(tradeShareRequest.getSymbol())
                .quantity(tradeShareRequest.getQuantity())
                .build();

        if (isBuy) {
            stockTransactionDTO.setBuyPrice(tradeShareRequest.getPrice());
        } else {
            stockTransactionDTO.setSellPrice(tradeShareRequest.getPrice());
        }

        return stockTransactionDTO;
    }
}
