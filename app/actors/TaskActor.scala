package actors

import akka.actor.Props
import service.TaskService
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import akka.actor.Actor
import akka.actor.ActorRef
import play.api.libs.json.Json
import models._
import play.api.mvc._
import controllers.Result
import forms.TaskForm

class TaskActor(out: ActorRef, taskService: TaskService) extends Actor {
	def receive = {
		case taskForm:TaskForm => taskForm.mode match{
			case "add" =>
				out ! Result(true,taskForm.mode, add(TaskForm.mappingTask(taskForm)));
			case "remove" =>
				val deleteId = remove(taskForm.id)
				out ! Result(true,taskForm.mode, String.valueOf(deleteId));
		}
		case _ => 
			out ! Result(false,"","")
	}
	def add(task:Task) = {
		taskService.addTask(task)
		val taskSeq = Await.result(taskService.getTaskById(task.id), Duration.Inf)
		Json.toJson(taskSeq).toString()
	}
	def remove(id:Int) = {
		taskService.removeTaskById(id)
	}
}

object TaskActor {
	def props(out: ActorRef, taskService: TaskService) = Props(classOf[TaskActor],out, taskService)
}
