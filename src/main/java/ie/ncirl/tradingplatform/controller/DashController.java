package ie.ncirl.tradingplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashController {

    @GetMapping({"/dash", "/"})
    public String showDash() {
        return "dash";
    }
}
