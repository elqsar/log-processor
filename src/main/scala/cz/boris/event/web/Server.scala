package cz.boris.event.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import akka.util.Timeout
import cz.boris.event.config.Configuration
import cz.boris.event.web.routes.{LogRoute, UploadRoute}

import scala.concurrent.duration._

class Server extends UploadRoute with LogRoute {
  implicit val system = ActorSystem("Analyzer")
  implicit val mat = ActorMaterializer()
  implicit val exec = system.dispatcher
  implicit val timeout = Timeout(5.seconds)

  def routes = upload ~ getLogs

  implicit def globalErrorHandler: ExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      complete(HttpResponse(InternalServerError, entity = s"Server error: ${ex.getLocalizedMessage}"))
  }

  def run() = {
    Http().bindAndHandle(routes, Configuration.host, Configuration.port)
  }

}
