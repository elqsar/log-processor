package cz.boris.event.repository

import cz.boris.event.config.Configuration
import cz.boris.event.model.LogEvent
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.{MultiBulkWriteResult, WriteResult}
import reactivemongo.api.{MongoConnection, FailoverStrategy, MongoDriver}
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import cz.boris.event.model.readers.Readers._
import cz.boris.event.model.writers.Writers._

import scala.util.{Failure, Success}

trait Repository {
  val collectionName = "logevents"
  val dbName = "logs"

  val driver = new MongoDriver()
  val connection:MongoConnection = MongoConnection.parseURI(Configuration.dbHost).map { parsed =>
    driver.connection(parsed)
  } match {
    case Success(conn) => conn
    case Failure(ex) => throw new RuntimeException(s"Could not establish the connection: $ex")
  }
  val strategy = FailoverStrategy(
    initialDelay = 15.seconds,
    retries = 5,
    delayFactor = attempt => 1 + attempt
  )

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
    val db = connection(dbName, failoverStrategy = strategy)
    db[BSONCollection](collectionName)
  }
}
