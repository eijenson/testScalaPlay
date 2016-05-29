package controllers

import dao.TaskDao
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.number
import play.api.data.Forms.text
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller

class Application @Inject() (taskDao: TaskDao) extends Controller {

	var accessNum: Integer = 0

	def index = Action {
		Redirect(routes.Application.task)
	}

	def randam = Action { implicit request =>
		accessNum += 1
		Ok(views.html.randam("回数" + accessNum))
	}
	var taskForm = Form(
		mapping(
			"id" -> number,
			"name" -> text,
			"ticket" -> text)(models.Task.apply)(models.Task.unapply))

	var deleteIdForm = Form("id" -> number)

	def task = Action.async { implicit request =>
		taskDao.all().map { case tasks => Ok(views.html.task(tasks)) }
	}

	def taskSubmit = Action.async { implicit request =>
		val task: models.Task = taskForm.bindFromRequest.get
		taskDao.insert(task).map(_ => Redirect(routes.Application.task))
	}

	def taskDelete = Action.async { implicit request =>
		val id: Int = deleteIdForm.bindFromRequest.get
		taskDao.delete(id).map(_ => Redirect(routes.Application.task))
	}
}
