package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

class Application extends Controller {

  var accessNum:Integer = 0
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def randam = Action{
    accessNum += 1
    Ok(views.html.randam("回数"+accessNum))
  }
  var taskForm = Form(
    mapping(
      "name" -> text,
      "ticket" -> text)(Task.apply)(Task.unapply))
  
  val defaultTask = Task("","")
  var DBtaskList:List[Task] = List(defaultTask)
  
  def task = Action{
    Ok(views.html.task(taskForm ,DBtaskList))
  }
  
  def taskSubmit = Action{ implicit request =>
    val task = taskForm.bindFromRequest.get
    var taskList:List[Task] = DBtaskList :+ task
    DBtaskList = taskList
    Ok(views.html.task(taskForm,taskList))
  }
}

case class Task(name:String , ticket:String)
