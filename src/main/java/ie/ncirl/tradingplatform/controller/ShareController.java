package ie.ncirl.tradingplatform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShareController {

	@GetMapping("/trade")
	public String hello() {
		return "hello";
	}
}
