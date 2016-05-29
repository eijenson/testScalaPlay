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

  //全てのタスクをリストで返す
  def all(): Future[Seq[Task]] = db.run(Tasks.result)

  //IDを元にタスクを取得する
  def findByid(id:Int): Future[Option[Task]] = db.run(Tasks.filter(_.id === id).result.headOption)
  
  //タスクを追加する
  def insert(task: Task) = db.run(Tasks += task)

  //IDを元にタスクを削除する
  def delete(id:Int): Future[Int] = db.run(Tasks.filter(_.id === id).delete)
  
  private class TaskTable(tag: Tag) extends Table[Task](tag, "task") {

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("name")
    def ticket = column[String]("ticket")

    def * = (id, name , ticket) <> (Task.tupled, Task.unapply)
  }
}