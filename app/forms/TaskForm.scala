package forms

import models.Task
import play.api.libs.json._

case class TaskForm (
	id: Int,
	name: String,
	ticket: String,
	mode: String
)

object TaskForm {
	implicit def jsonWrites = Json.writes[TaskForm]
	implicit def jsonReads = Json.reads[TaskForm]
	def tupled = (TaskForm.apply _).tupled
	def mappingTask(taskForm:TaskForm) : Task = new Task(taskForm.id,taskForm.name,taskForm.ticket);
}