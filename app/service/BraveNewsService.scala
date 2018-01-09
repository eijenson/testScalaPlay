package service

import javax.inject.Inject

import dao.BraveNewsDao
import javax.inject.Singleton

import models.BraveNews

import scala.concurrent.Future

@Singleton
class BraveNewsService @Inject()(braveNewsDao: BraveNewsDao) {
  def addBraveNews(braveNews: BraveNews) {
    braveNewsDao.insert(braveNews)
  }

  def getAllBraveNews(): Future[Seq[BraveNews]] = {
    braveNewsDao.all()
  }

  def getBraveNewsById(id: Long): Future[Option[BraveNews]] = {
    braveNewsDao.findByid(id)
  }

  def removeBraveNewsById(id: Long): Future[Int] = {
    braveNewsDao.delete(id)
  }
}