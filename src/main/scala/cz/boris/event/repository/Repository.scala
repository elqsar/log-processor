package cz.boris.event.repository

import cz.boris.event.model.LogEvent
import cz.boris.event.model.readers.Readers._
import cz.boris.event.model.writers.Writers._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.MultiBulkWriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

trait Repository {

  def persist(events: Seq[LogEvent])(implicit exec: ExecutionContext): Future[MultiBulkWriteResult] = {
    val collection = dbWithCollection
    val bulkDocs = events.map(implicitly[collection.ImplicitlyDocumentProducer](_))
    collection.bulkInsert(ordered = false)(bulkDocs: _*)
  }

  def readAll(limit: Int)(implicit exec: ExecutionContext): Future[List[LogEvent]] = {
    val query = BSONDocument()
    dbWithCollection
      .find(query)
      .cursor[LogEvent]()
      .collect[List](limit)
  }

  def readByMessage(message: String)(implicit exec: ExecutionContext): Future[List[LogEvent]] = {
    val query = BSONDocument("message" -> BSONDocument("$regex" -> s".*$message.*"))
    dbWithCollection
      .find(query)
      .cursor[LogEvent]()
      .collect[List](25)
  }

  private def dbWithCollection = {
    val db = Database.connection(Database.dbName, failoverStrategy = Database.strategy)
    db[BSONCollection](Database.collectionName)
  }
}
