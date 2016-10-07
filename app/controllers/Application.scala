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
		Redirect(routes.TaskController.task)
	}

	def randam = Action { implicit request =>
		accessNum += 1
		Ok(views.html.randam("回数" + accessNum))
	}


}
