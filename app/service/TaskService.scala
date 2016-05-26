package service

import javax.inject.Inject
import dao.TaskDao
import javax.inject.Singleton
import models.Task

@Singleton
class TaskService  @Inject()(taskDao:TaskDao){
  def addTask(task: Task){
    taskDao.insert(task)
  }
  
  def getAllTask(){
    taskDao.all()
  }
}