package cz.boris.event

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.{ImplicitSender, TestKit}
import cz.boris.event.model.LogEvent
import cz.boris.event.service.LogEventPersistFlow
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import reactivemongo.api.commands.MultiBulkWriteResult

import scala.concurrent.{ExecutionContext, Future}

class LogEventPersistFlowSpec extends TestKit(ActorSystem("test"))
with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll
with LogEventPersistFlow {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  override def persist(events: Seq[LogEvent])(implicit exec: ExecutionContext): Future[MultiBulkWriteResult] = {
    Future.successful(MultiBulkWriteResult())
  }

  implicit val mat = ActorMaterializer()
  implicit val exec = system.dispatcher

  "Events" should "be persisted properly" in {
    val events = List(LogEvent(
      "09:33:19.458",
      "[RGT-system-akka.actor.default-dispatcher-81]",
      "INFO",
      "Processor",
      "Processing XY contact: 777"
    ))

    Source(events)
      .grouped(1)
      .via(persistEvent)
      .runWith(TestSink.probe[MultiBulkWriteResult])
      .request(1)
      .expectNext(MultiBulkWriteResult())
  }

}
