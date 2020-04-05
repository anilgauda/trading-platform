package ie.ncirl.tradingplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/")
    public String showDash() {
        return "dash";
    }

    // All ui routes except dashboard are prefixed by app so that they can be configured as excluded in spring security

    @GetMapping({"/app/login"})
    public String showLogin() {
        return "login";
    }

    @GetMapping({"/app/register"})
    public String showRegister() {
        return "register";
    }

    @GetMapping({"/app/trade"})
    public String showSharesDetail() {
        return "shares";
    }

    @GetMapping({"/app/stocks"})
    public String showMyStocks() {
        return "mystocks";
    }

    @GetMapping({"/app/history"})
    public String showHistory() {
        return "history";
    }

    @GetMapping({"/app/goals"})
    public String showGoals() {
        return "goals";
    }

    @GetMapping({"/app/account"})
    public String showAccount() {
        return "account";
    }


    @GetMapping({"/app/reports"})
    public String showReports() {
        return "reports";
    }
}
