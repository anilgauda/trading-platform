package ie.ncirl.tradingplatform.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        String json = "[{\"name\":\"Amazon.com, Inc.\",\"symbol\":\"AMZN\",\"mcap\":9.53893584896E11,\"price\":1916.18,\"open\":1932.97,\"prevClose\":1949.72,\"vol\":2723961.0,\"eps\":23.01,\"pe\":83.27597,\"bookVal\":124.618,\"buy\":0},{\"name\":\"International Business Machines Corporation\",\"symbol\":\"IBM\",\"mcap\":9.5246221312E10,\"price\":107.21,\"open\":106.36,\"prevClose\":110.93,\"vol\":3129988.0,\"eps\":10.566,\"pe\":10.146697,\"bookVal\":23.493,\"buy\":0},{\"name\":\"Intel Corporation\",\"symbol\":\"INTC\",\"mcap\":2.26381611008E11,\"price\":52.93,\"open\":52.5,\"prevClose\":54.12,\"vol\":1.604083E7,\"eps\":4.71,\"pe\":11.237792,\"bookVal\":18.066,\"buy\":0},{\"name\":\"Apple Inc.\",\"symbol\":\"AAPL\",\"mcap\":1.061972738048E12,\"price\":242.71,\"open\":246.5,\"prevClose\":254.29,\"vol\":2.8069909E7,\"eps\":12.595,\"pe\":19.270346,\"bookVal\":20.418,\"buy\":0},{\"name\":\"Alphabet Inc.\",\"symbol\":\"GOOGL\",\"mcap\":7.62204585984E11,\"price\":1108.6,\"open\":1124.0,\"prevClose\":1161.95,\"vol\":1428800.0,\"eps\":49.163,\"pe\":22.549479,\"bookVal\":292.651,\"buy\":0},{\"name\":\"Johnson Controls International plc\",\"symbol\":\"JCI\",\"mcap\":1.926081536E10,\"price\":25.2162,\"open\":25.79,\"prevClose\":26.96,\"vol\":4374771.0,\"eps\":6.55,\"pe\":3.8498013,\"bookVal\":25.298,\"buy\":0},{\"name\":\"CRH plc\",\"symbol\":\"CRH\",\"mcap\":2.0759322624E10,\"price\":26.1116,\"open\":25.94,\"prevClose\":26.84,\"vol\":1436001.0,\"eps\":4.135,\"pe\":6.314776,\"bookVal\":21.473,\"buy\":0},{\"name\":\"Medtronic plc\",\"symbol\":\"MDT\",\"mcap\":1.14423709696E11,\"price\":85.38,\"open\":91.68,\"prevClose\":90.18,\"vol\":4861759.0,\"eps\":4.01,\"pe\":21.291769,\"bookVal\":38.64,\"buy\":0},{\"name\":\"Microsoft Corporation\",\"symbol\":\"MSFT\",\"mcap\":1.16585529344E12,\"price\":153.28,\"open\":153.0,\"prevClose\":157.71,\"vol\":3.6522492E7,\"eps\":5.741,\"pe\":26.69918,\"bookVal\":14.467,\"buy\":0},{\"name\":\"Eaton Corporation plc\",\"symbol\":\"ETN\",\"mcap\":2.9903241216E10,\"price\":72.68,\"open\":72.57,\"prevClose\":77.69,\"vol\":1939359.0,\"eps\":5.25,\"pe\":13.843809,\"bookVal\":38.911,\"buy\":0},{\"name\":\"Facebook, Inc.\",\"symbol\":\"FB\",\"mcap\":4.56171749376E11,\"price\":160.035,\"open\":161.615,\"prevClose\":166.8,\"vol\":1.2904943E7,\"eps\":6.43,\"pe\":24.888803,\"bookVal\":35.433,\"buy\":0},{\"name\":\"Allergan plc\",\"symbol\":\"AGN\",\"mcap\":5.768062976E10,\"price\":175.32,\"open\":173.49,\"prevClose\":177.1,\"vol\":1726474.0,\"eps\":-16.021,\"pe\":0.0,\"bookVal\":177.035,\"buy\":0},{\"name\":\"Dell Technologies Inc.\",\"symbol\":\"DELL\",\"mcap\":2.759716864E10,\"price\":37.32,\"open\":37.75,\"prevClose\":39.55,\"vol\":1409507.0,\"eps\":6.035,\"pe\":6.183927,\"bookVal\":-2.118,\"buy\":0},{\"name\":\"Morgan Stanley Emerging Markets Debt Fund, Inc.\",\"symbol\":\"MSD\",\"mcap\":1.51903344E8,\"price\":7.4511,\"open\":7.4,\"prevClose\":7.65,\"vol\":60789.0,\"eps\":1.348,\"pe\":5.527522,\"bookVal\":10.55,\"buy\":0},{\"name\":\"Oracle Corporation\",\"symbol\":\"ORCL\",\"mcap\":1.52349458432E11,\"price\":48.31,\"open\":46.55,\"prevClose\":48.33,\"vol\":9391732.0,\"eps\":3.18,\"pe\":15.191824,\"bookVal\":4.503,\"buy\":0},{\"name\":\"Pfizer Inc.\",\"symbol\":\"PFE\",\"mcap\":1.76745037824E11,\"price\":31.8595,\"open\":31.92,\"prevClose\":32.64,\"vol\":1.4238231E7,\"eps\":2.867,\"pe\":11.112487,\"bookVal\":11.407,\"buy\":0},{\"name\":\"Boston Scientific Corporation\",\"symbol\":\"BSX\",\"mcap\":4.2395611136E10,\"price\":30.365,\"open\":31.62,\"prevClose\":32.63,\"vol\":5927842.0,\"eps\":3.33,\"pe\":9.118619,\"bookVal\":9.948,\"buy\":0},{\"name\":\"TOTAL S.A.\",\"symbol\":\"TOT\",\"mcap\":1.03108984832E11,\"price\":38.23,\"open\":38.35,\"prevClose\":37.24,\"vol\":2834148.0,\"eps\":4.17,\"pe\":9.167866,\"bookVal\":45.151,\"buy\":0},{\"name\":\"J. C. Penney Company, Inc.\",\"symbol\":\"JCP\",\"mcap\":1.01622904E8,\"price\":0.3166,\"open\":0.35,\"prevClose\":0.36,\"vol\":7323270.0,\"eps\":-0.84,\"pe\":0.0,\"bookVal\":2.587,\"buy\":0},{\"name\":\"Perrigo Company plc\",\"symbol\":\"PRGO\",\"mcap\":6.17744384E9,\"price\":45.38,\"open\":47.05,\"prevClose\":48.09,\"vol\":1001088.0,\"eps\":1.07,\"pe\":42.411213,\"bookVal\":42.644,\"buy\":0},{\"name\":\"VMware, Inc.\",\"symbol\":\"VMW\",\"mcap\":4.856428544E10,\"price\":116.18,\"open\":116.88,\"prevClose\":121.1,\"vol\":707417.0,\"eps\":15.08,\"pe\":7.704244,\"bookVal\":16.78,\"buy\":0},{\"name\":\"Tesla, Inc.\",\"symbol\":\"TSLA\",\"mcap\":8.9852755968E10,\"price\":487.3,\"open\":504.0,\"prevClose\":524.0,\"vol\":9253402.0,\"eps\":-4.92,\"pe\":0.0,\"bookVal\":36.564,\"buy\":0},{\"name\":\"The Long-Term Care ETF\",\"symbol\":\"OLD\",\"mcap\":0.0,\"price\":18.0326,\"open\":18.84,\"prevClose\":19.847,\"vol\":11534.0,\"eps\":0.0,\"pe\":0.0,\"bookVal\":0.0,\"buy\":0},{\"name\":\"NVIDIA Corporation\",\"symbol\":\"NVDA\",\"mcap\":1.5362424832E11,\"price\":251.02,\"open\":255.65,\"prevClose\":263.6,\"vol\":1.0817578E7,\"eps\":4.52,\"pe\":55.5354,\"bookVal\":19.925,\"buy\":0},{\"name\":\"Advanced Micro Devices, Inc.\",\"symbol\":\"AMD\",\"mcap\":5.2044283904E10,\"price\":44.45,\"open\":44.18,\"prevClose\":45.48,\"vol\":7.0589535E7,\"eps\":0.3,\"pe\":148.16666,\"bookVal\":2.416,\"buy\":0},{\"name\":\"QUALCOMM Incorporated\",\"symbol\":\"QCOM\",\"mcap\":7.606931456E10,\"price\":66.55,\"open\":65.0,\"prevClose\":67.65,\"vol\":8391692.0,\"eps\":3.524,\"pe\":18.88479,\"bookVal\":3.948,\"buy\":0},{\"name\":\"Adobe Inc.\",\"symbol\":\"ADBE\",\"mcap\":1.47048792064E11,\"price\":304.28,\"open\":307.0,\"prevClose\":318.24,\"vol\":2266121.0,\"eps\":6.599,\"pe\":46.110016,\"bookVal\":21.667,\"buy\":0},{\"name\":\"ASML Holding N.V.\",\"symbol\":\"ASML\",\"mcap\":1.09356761088E11,\"price\":251.37,\"open\":253.13,\"prevClose\":261.64,\"vol\":418978.0,\"eps\":6.959,\"pe\":36.121567,\"bookVal\":30.625,\"buy\":0},{\"name\":\"Autodesk, Inc.\",\"symbol\":\"ADSK\",\"mcap\":3.0904164352E10,\"price\":140.78,\"open\":147.53,\"prevClose\":156.1,\"vol\":1684219.0,\"eps\":0.96,\"pe\":146.64583,\"bookVal\":-0.634,\"buy\":0},{\"name\":\"Cisco Systems, Inc.\",\"symbol\":\"CSCO\",\"mcap\":1.642704896E11,\"price\":38.735,\"open\":38.17,\"prevClose\":39.31,\"vol\":1.4546334E7,\"eps\":2.564,\"pe\":15.107255,\"bookVal\":8.378,\"buy\":0}]";
        Gson gson = new Gson();
        List<SharesTableVo> shares = gson.fromJson(json, new TypeToken<List<SharesTableVo>>() {
        }.getType());
        //List<SharesTableVo> shares = shareService.getAllShares();
        return shares;
    }

    @GetMapping("/trade/my")
    public List<MyStockVo> getMyStocks() throws IOException {
        return  shareService.getSharesForAccount(userService.getActiveAccount());
    }


    @PostMapping("/trade/share/buy")
    public ResponseDTO<String> buyStock(TradeShareRequest tradeShareRequest) {
        sqsClientService.send(createStockTransactionDTO(tradeShareRequest, true));
        return new ResponseDTO<String>().withData("OK");
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
