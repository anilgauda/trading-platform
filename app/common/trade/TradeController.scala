package common.trade

import com.google.gson.{Gson, GsonBuilder}
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.{Action, AnyContent}

class TradeController @Inject() (val tcc: TradeControllerComponents)extends TradeBaseController(tcc){
def index(tickerCode: String): Action[AnyContent]= Action{implicit request =>
  val gson=new GsonBuilder().setPrettyPrinting().create()
  val tradeApiConsumer=new TradeApiConsumer
  val details=tradeApiConsumer.getStock(tickerCode)
  println(details.toString())
  //Ok(Json.toJson(stockWriter.writes(details)))
  Ok(gson.toJson(details))
}
}