package ie.ncirl.tradingplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
	 
    @GetMapping({"/dashboard", "/"})
    public String showDash() {
        return "dash";
    }

    // All ui routes except dashboard are prefixed by app so that they can be configured as excluded in spring security

    @GetMapping({"/app/trade"})
    public String showSharesDetail() {
        return "shares";
    }

    @GetMapping({"/app/stocks"})
    public String showMyStocks() {
        return "mystocks";
    }

}
