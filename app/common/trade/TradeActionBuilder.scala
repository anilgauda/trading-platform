package common.trade

import com.google.inject.Inject
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{ActionBuilder, AnyContent, BaseController, ControllerComponents, DefaultActionBuilder, PlayBodyParsers, Request}

import scala.concurrent.{ExecutionContext, Future}

class TradeActionBuilder

case class TradeBaseController @Inject() (cc: TradeControllerComponents) extends BaseController {
  override protected def controllerComponents: ControllerComponents = cc
}

case class TradeControllerComponents @Inject() (actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext)() extends ControllerComponents {
}

