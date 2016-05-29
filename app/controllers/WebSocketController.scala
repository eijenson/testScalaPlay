package controllers

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.stream.Materializer
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import play.api.mvc.WebSocket.MessageFlowTransformer

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