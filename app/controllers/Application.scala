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
    Redirect(routes.Application.task)
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
        
  var deleteIdForm = Form("id" -> number)
      
  def task = Action.async{
    taskDao.all().map{case tasks => Ok(views.html.task(tasks))}
  }
  
  def taskSubmit = Action.async{ implicit request =>
    val task : models.Task = taskForm.bindFromRequest.get
    taskDao.insert(task).map(_  => Redirect(routes.Application.task))
  }
  
  def taskDelete = Action.async{ implicit request =>
    val id : Int =  deleteIdForm.bindFromRequest.get
    taskDao.delete(id).map(_ => Redirect(routes.Application.task))
  }
}
