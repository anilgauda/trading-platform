package ie.ncirl.tradingplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
	 
    @GetMapping({"/dashboard", "/"})
    public String showDash() {
        return "dash";
    }
    
    @GetMapping({"/trade"})
    public String showSharesDetail() {
        return "shares";
    }
}
