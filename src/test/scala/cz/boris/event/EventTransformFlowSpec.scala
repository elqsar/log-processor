package cz.boris.event

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.{ImplicitSender, TestKit}
import cz.boris.event.model.LogEvent
import cz.boris.event.service.LogEventTransformFlow
import org.scalatest._

class EventTransformFlowSpec extends TestKit(ActorSystem("test"))
with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll
with LogEventTransformFlow {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  implicit val mat = ActorMaterializer()

  "Event" should "transform EventLog properly" in {
    val source = "09:33:19.458 [RGT-system-akka.actor.default-dispatcher-81] INFO  c.g.a.p.Processor - Processing XY contact: 777"
    val expectedResult = LogEvent(
      "09:33:19.458",
      "[RGT-system-akka.actor.default-dispatcher-81]",
      "INFO",
      "Processor",
      "Processing XY contact: 777"
    )

    Source(List(source))
      .via(balancedMap)
      .runWith(TestSink.probe[LogEvent])
      .request(1)
      .expectNext(expectedResult)
      .expectComplete()
  }
}
