package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import javax.inject.Inject
import dao.TaskDao
import models.Task
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application @Inject()(taskDao: TaskDao) extends Controller {

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
      "id"   -> number,
      "name" -> text,
      "ticket" -> text)(models.Task.apply)(models.Task.unapply))
  
      
      
      
      

  def task = Action.async{
    taskDao.all().map{case tasks => Ok(views.html.task(tasks))}
  }
  
  def taskSubmit = Action.async{ implicit request =>
    val task : models.Task = taskForm.bindFromRequest.get
    //Ok(views.html.task(taskForm,taskList))
    taskDao.insert(task).map(_  => Redirect(routes.Application.task))
  }
}
