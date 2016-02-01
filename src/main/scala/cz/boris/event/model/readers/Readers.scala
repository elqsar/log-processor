package cz.boris.event.model.readers

import cz.boris.event.model.LogEvent
import reactivemongo.bson.{Macros, BSONDocumentReader}

object Readers {
  implicit val logEventReader: BSONDocumentReader[LogEvent] = Macros.reader[LogEvent]
}
