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
import models.Task
import dao.TaskDao
import service.TaskService



class TaskController @Inject() (implicit actorSystem: ActorSystem, materializer: Materializer,taskService: TaskService) extends Controller {
  implicit val inEventFormat = Json.format[Task]
  implicit val outEventFormat = Json.format[Result]
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[Task, Result]

  def addTask = WebSocket.accept[Task, Result] { implicit request =>
    ActorFlow.actorRef[Task, Result] { out =>
      TaskSocketActor.props(out,taskService)
    }
  }
  
  /*def task = WebSocket.accept[Task, Result] { implicit request =>
    ActorFlow.actorRef[Task, Result] { out =>
      TaskSocketActor.props(out,taskService)
    }
  }*/

}
case class Result(result: String = "success")

object TaskSocketActor {
  def props(out: ActorRef,taskService: TaskService) = Props(new TaskSocketActor(out,taskService))
}


class TaskSocketActor(out: ActorRef,taskService: TaskService) extends Actor {
  def receive = {
    case task: Task =>
      out ! Result("success")
      taskService.addTask(task)
  }
}