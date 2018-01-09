package models

import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json, Writes}


case class BraveNews(
                      id: Long = 0,
                      title: String,
                      detail: String,
                      period: String,
                      url: String,
                      startTime: DateTime = new DateTime(),
                      endTime: DateTime = new DateTime(),
                      createTime: DateTime = new DateTime(),
                      isViewingSite: Boolean = true
                    )

object BraveNews {
  implicit val dateTimeWriter: Writes[DateTime] = JodaWrites.jodaDateWrites("dd/MM/yyyy HH:mm:ss")

  implicit def jsonWrites = Json.writes[BraveNews]

  implicit val dateTimeReader = JodaReads.jodaDateReads("yyyyMMddHHmmss")

  implicit def jsonReads = Json.reads[BraveNews]

  def tupled = (BraveNews.apply _).tupled
}