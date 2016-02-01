package cz.boris.event.model.writers

import cz.boris.event.model.LogEvent
import reactivemongo.bson.{Macros, BSONDocumentWriter}

object Writers {
  implicit val logEventWriter: BSONDocumentWriter[LogEvent] = Macros.writer[LogEvent]
}
