package cz.boris.event.web.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import cz.boris.event.repository.Repository
import org.json4s.{DefaultFormats, jackson}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

import scala.concurrent.ExecutionContext

trait LogRoute extends Repository {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def getLogs(implicit exec: ExecutionContext) =
    path("logs") {
      parameters('size.as[Int]) { size =>
        onSuccess(readAll(size)) { resp =>
          complete(resp)
        }
      }
    }

}
