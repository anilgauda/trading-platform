package ie.ncirl.tradingplatform.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ie.ncirl.tradingplatform.service.SQSClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ie.ncirl.tradingplatform.service.ShareService;
import ie.ncirl.tradingplatform.vo.SharesTableVo;
import ie.ncirl.tradingplatform.vo.SharesVo;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

@Slf4j
@RestController
public class ShareController {
	
	@Autowired
	ShareService shareService;
	
	@Autowired
	private SQSClientService sqsClientService;
	@GetMapping("/trade")
	public SharesVo getStocks(@RequestParam(value="user") String user,@RequestParam(value="name")String name) throws IOException {
		log.info("user and name from request {},{}",user,name);
		Stock shares=shareService.getShareDetails(name);
		SharesVo sharesVo=new SharesVo();
		sharesVo.setName(shares.getName());
		ArrayList<Double> close=new ArrayList<>();
		ArrayList<Double> open=new ArrayList<>();
		ArrayList<Double> high=new ArrayList<>();
		ArrayList<Double> low=new ArrayList<>();
		ArrayList<Double> volume=new ArrayList<>();
		ArrayList<String> dates=new ArrayList<>();
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yy");
		for(HistoricalQuote histStock:shares.getHistory()) {
			close.add(histStock.getClose().doubleValue());
			open.add(histStock.getOpen().doubleValue());
			high.add(histStock.getHigh().doubleValue());
			low.add(histStock.getLow().doubleValue());
			volume.add(histStock.getVolume().doubleValue());
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
		List<SharesTableVo> shares=shareService.getAllShares();
		return shares;
	}
	
	@GetMapping("/stock/buy")
	public void buyStock() {
		sqsClientService.send();
	}
}
