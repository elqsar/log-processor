package cz.boris.event.web.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import cz.boris.event.repository.LogRepository
import org.json4s.{DefaultFormats, jackson}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

import scala.concurrent.ExecutionContext

case class LogCriteria(message: String)

trait LogRoute extends LogRepository {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def getLogs(implicit exec: ExecutionContext) =
    path("logs") {
      get {
        parameters('size.as[Int]) { size =>
          onSuccess(readAll(size)) { resp =>
            complete(resp)
          }
        }
      }
    }

  def findByMessage(implicit exec: ExecutionContext) =
    path("logs") {
      post {
        entity(as[LogCriteria]) { criteria =>
          onSuccess(readByMessage(criteria.message)) { resp =>
            complete(resp)
          }
        }
      }
    }

}
