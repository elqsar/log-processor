package cz.boris.event.web.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.io.Framing
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import cz.boris.event.service.{FileValidator, LogEventPersistFlow, LogEventTransformFlow}

import scala.concurrent.ExecutionContext

trait UploadRoute extends FileValidator
  with LogEventTransformFlow
  with LogEventPersistFlow {

  def upload(implicit mat: ActorMaterializer, exec: ExecutionContext) =
    path("uploads") {
      fileUpload("logs") { case (meta, source) =>
        validate(isLogFile(meta.fileName), "Unknown file extension") {
          val result = source
            .via(Framing.delimiter(ByteString("\n"), 2 << 12))
            .map(_.utf8String)
            .via(balancedMap)
            .grouped(2000)
            .via(persistEvent)
            .runWith(Sink.last)

          onSuccess(result) { res =>
            complete(HttpResponse(OK, entity = "Upload success"))
          }
        }
      }
    }
}
