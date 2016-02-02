package cz.boris.event.service

import akka.stream.{SourceShape, FlowShape}
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl._
import cz.boris.event.model.LogEvent

trait LogEventTransformFlow {

  def mapToLogEvent =
    Flow[String].map { rawEvent =>
      val pattern = """(\d+.\d+.\d+.\d+) (\[.+\]) ([A-Z]+) (.+) (\-.+)""".r
      val pattern(time, thread, info, klass, message) = rawEvent.trim

      LogEvent(time, thread, info, className(klass), cleanMessage(message))
    }

  def balancedMap =
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      val balancer = b.add(Balance[String](3))
      val merge = b.add(Merge[LogEvent](3))

      balancer.out(0) ~> mapToLogEvent ~> merge.in(0)
      balancer.out(1) ~> mapToLogEvent ~> merge.in(1)
      balancer.out(2) ~> mapToLogEvent ~> merge.in(2)

      FlowShape(balancer.in, merge.out)
    })

  private def className(body: String) = body.substring(body.lastIndexOf(".") + 1)

  private def cleanMessage(body: String) = body.replace("- ", "")

}
