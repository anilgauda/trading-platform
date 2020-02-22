package common.trade

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class TradeRouter @Inject() (controller: TradeController)extends SimpleRouter{
  val prefix= "/trade"


  override def routes: Routes = {
    case GET(p"/$tickerCode") =>
      controller.index(tickerCode)
  }
}
