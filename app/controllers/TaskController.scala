package controllers

import actors.TaskActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.stream.Materializer
import dao.TaskDao
import javax.inject._
import models.Task
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import service.TaskService
import akka.actor.ActorSystem
import forms.TaskForm

@Singleton
class TaskController @Inject()(implicit actorSystem: ActorSystem, materializer: Materializer, taskService: TaskService) extends Controller {
  implicit val inEventFormat = Json.format[TaskForm]
  implicit val outEventFormat = Json.format[Result]
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[TaskForm, Result]

  def task = Action.async { implicit request =>
    taskService.getAllTask().map { case tasks => Ok(views.html.task(tasks)) }
  }

  def wsTask = WebSocket.accept[TaskForm, Result] { request =>
    ActorFlow.actorRef[TaskForm, Result] { out =>
      TaskActor.props(out, taskService)
    }
  }

  def taskJson = Action.async { request =>
    taskService.getAllTask().map { case tasks => Ok(Json.toJson(tasks))
    }
  }
}

case class Result(result: Boolean, mode: String, data: String)