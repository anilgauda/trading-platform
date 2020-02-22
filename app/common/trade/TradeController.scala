package common.trade

import javax.inject.Inject
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.{Action, AnyContent}

class TradeController @Inject() (val tcc: TradeControllerComponents)extends TradeBaseController(tcc){
def index(tickerCode: String): Action[AnyContent]= Action{implicit request =>
  val tradeApiConsumer=new TradeApiConsumer
  val details=tradeApiConsumer.getStock(tickerCode)
  println(details.toString())
  implicit val stockWriter= new Writes[Share] {
    override def writes(o: Share): JsValue = Json.obj(
      "name" -> o.name,
      "tickercode" ->o.tickerCode,
      "price" ->o.price,
      "avgprice50" ->o.avgPrice50,
      "avgprice200" ->o.avgPrice200,
      "ebidta" ->o.ebitda,
      "oneyeartarget" ->o.oneYearTargetPrice
    )
  }
  Ok(Json.toJson(stockWriter.writes(details)))
}
}