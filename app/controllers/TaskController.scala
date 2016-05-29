package controllers

import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc._
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.libs.streams.ActorFlow
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Actor
import models.Task
import dao.TaskDao
import service.TaskService
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Json

class TaskController @Inject() (implicit actorSystem: ActorSystem, materializer: Materializer, taskService: TaskService) extends Controller {
	implicit val inEventFormat = Json.format[Task]
	implicit val outEventFormat = Json.format[Result]
	implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[Task, Result]

	def addTask = WebSocket.accept[Task, Result] { implicit request =>
		ActorFlow.actorRef[Task, Result] { out =>
			TaskSocketActor.props(out, taskService)
		}
	}
}

case class Result(result: String = "success", data: String)

object TaskSocketActor {
	def props(out: ActorRef, taskService: TaskService) = Props(new TaskSocketActor(out, taskService))
}

class TaskSocketActor(out: ActorRef, taskService: TaskService) extends Actor {
	def receive = {
		case task: Task =>
			taskService.addTask(task)
			val taskSeq = Await.result(taskService.getTaskById(task.id), Duration.Inf)
			val taskJson = Json.toJson(taskSeq).toString()
			out ! Result("success", taskJson)
	}
}