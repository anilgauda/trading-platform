package ie.ncirl.TradingPlatform.controller

import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

@RestController
@RequestMapping(path = Array("/"))
class DashboardController {

  @GetMapping(path = Array("/"))
  def getDashBoard(): String={
  return "Hello Scala"
  }
}
