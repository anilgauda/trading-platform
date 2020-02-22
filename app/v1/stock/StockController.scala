package v1.stock

import javax.inject.Inject
import play.api.Logger
import play.api.mvc._
import services.SQSClient

import scala.concurrent.ExecutionContext

/**
 * Takes HTTP requests and produces JSON.
 */
class StockController @Inject()(cc: ControllerComponents, sqs: SQSClient)(
  implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  private val logger = Logger(getClass)

  def buy: Action[AnyContent] = Action {
    sqs.send
    Ok("it works")
  }
}
