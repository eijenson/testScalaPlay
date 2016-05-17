package dao

import scala.concurrent.Future
import javax.inject.Inject

import models.Task

import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile



class TaskDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Tasks = TableQuery[TaskTable]

  def all(): Future[Seq[Task]] = db.run(Tasks.result)

  def insert(task: Task): Future[Unit] = db.run(Tasks += task).map {_ => ()}

  private class TaskTable(tag: Tag) extends Table[Task](tag, "task") {

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("name")
    def ticket = column[String]("ticket")

    def * = (id, name , ticket) <> (Task.tupled, Task.unapply _)
  }
}