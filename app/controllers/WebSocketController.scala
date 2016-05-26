package controllers

import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc._
import play.api.libs.json.Json
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.libs.streams.ActorFlow
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Actor



class WebSocketController @Inject() (implicit actorSystem: ActorSystem, materializer: Materializer) extends Controller {
  implicit val inEventFormat = Json.format[Ping]
  implicit val outEventFormat = Json.format[Pong]
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[Ping, Pong]

  def socket = WebSocket.accept[Ping, Pong] { request =>
    ActorFlow.actorRef[Ping, Pong] { out =>
      MyWebSocketActor.props(out)
    }
  }
}

case class Ping(ping: String = "ping")

case class Pong(pong: String = "pong")

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case ping: Ping =>
      out ! Pong(ping.ping)
  }
}