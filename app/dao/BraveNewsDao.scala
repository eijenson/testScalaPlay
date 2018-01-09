package dao

import java.sql.Timestamp

import scala.concurrent.Future
import javax.inject.Inject

import models.BraveNews
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

class BraveNewsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val BraveNewsTable = TableQuery[BraveNewsTable]

  //全てのタスクをリストで返す
  def all(): Future[Seq[BraveNews]] = db.run(BraveNewsTable.result)

  //IDを元にタスクを取得する
  def findByid(id: Long): Future[Option[BraveNews]] = db.run(BraveNewsTable.filter(_.id === id).result.headOption)

  //タスクを追加する
  def insert(braveNews: BraveNews) = db.run(BraveNewsTable += braveNews)

  //IDを元にタスクを削除する
  def delete(id: Long): Future[Int] = db.run(BraveNewsTable.filter(_.id === id).delete)

  implicit val jodatimeColumnType = MappedColumnType.base[DateTime, Timestamp](
    { jodatime => new Timestamp(jodatime.getMillis()) },
    { sqltime => new DateTime(sqltime.getTime) }
  )

  private class BraveNewsTable(tag: Tag) extends Table[BraveNews](tag, "braveNews") {

    def id = column[Long]("id", O.PrimaryKey ,O.AutoInc)

    def title = column[String]("title")

    def detail = column[String]("detail")

    def period = column[String]("period")

    def url = column[String]("url")

    def startTime = column[DateTime]("startTime")

    def endTime = column[DateTime]("endTime")

    def createTime = column[DateTime]("createTime")

    def isViewingSite = column[Boolean]("isViewingSite")

    def * = (id, title, detail, period, url, startTime, endTime, createTime, isViewingSite) <> (BraveNews.tupled, BraveNews.unapply)
  }

}