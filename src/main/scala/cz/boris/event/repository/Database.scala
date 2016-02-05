package cz.boris.event.repository

import cz.boris.event.config.Configuration
import reactivemongo.api.{FailoverStrategy, MongoConnection, MongoDriver}

import scala.util.{Failure, Success}
import scala.concurrent.duration._

object Database {
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
}
