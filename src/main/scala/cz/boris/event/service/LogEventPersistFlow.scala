package cz.boris.event.service

import akka.stream.scaladsl.Flow
import cz.boris.event.model.LogEvent
import cz.boris.event.repository.Repository
import reactivemongo.api.commands.{MultiBulkWriteResult, WriteResult}

import scala.concurrent.{ExecutionContext, Future}

trait LogEventPersistFlow extends Repository {

  def persistEvent(implicit exec: ExecutionContext): Flow[Seq[LogEvent], MultiBulkWriteResult, Unit] =
    Flow[Seq[LogEvent]].mapAsync(4)(events => persist(events))

}
