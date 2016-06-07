package service

import javax.inject.Inject
import dao.TaskDao
import javax.inject.Singleton
import models.Task
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Json

@Singleton
class TaskService @Inject() (taskDao: TaskDao) {
	def addTask(task: Task) {
		taskDao.insert(task)
	}

	def getAllTask(): Future[Seq[Task]] = {
		taskDao.all()
	}

	def getTaskById(id: Int): Future[Option[Task]] = {
		taskDao.findByid(id)
	}
	
	def removeTaskById(id: Int) : Future[Int] = {
		taskDao.delete(id)
	}
}