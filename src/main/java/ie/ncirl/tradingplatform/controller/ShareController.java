package ie.ncirl.tradingplatform.controller;

import ie.ncirl.tradingplatform.service.SQSClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShareController {

	@Autowired
	private SQSClientService sqsClientService;

	@GetMapping("/trade")
	public String hello() {
		return "hello";
	}

	@GetMapping("/stock/buy")
	public void buyStock() {
		sqsClientService.send();
	}
}
