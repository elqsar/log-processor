package cz.boris.event.service

import akka.stream.scaladsl.Flow
import cz.boris.event.model.LogEvent

trait LogEventTransformFlow {

  def mapToLogEvent =
    Flow[String].map { rawEvent =>
      val pattern = """(\d+.\d+.\d+.\d+) (\[.+\]) ([A-Z]+) (.+) (\-.+)""".r
      val pattern(time, thread, info, klass, message) = rawEvent.trim

      LogEvent(time, thread, info, className(klass), cleanMessage(message))
    }

  private def className(body: String) = body.substring(body.lastIndexOf(".") + 1)
  private def cleanMessage(body: String) = body.replace("- ", "")

}
