package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Task(
	id: Int,
	name: String,
	ticket: String
)

object Task {
	implicit def jsonWrites = Json.writes[Task]
	implicit def jsonReads = Json.reads[Task]
	def tupled = (Task.apply _).tupled
}