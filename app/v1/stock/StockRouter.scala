package v1.stock

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
 * Routes and URLs to the Stock controller.
 */
class StockRouter @Inject()(controller: StockController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/buy") =>
      controller.buy
  }
}