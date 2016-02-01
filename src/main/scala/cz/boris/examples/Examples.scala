package cz.boris.examples

import play.api.libs.iteratee.{Enumerator, Iteratee}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{QueryOpts, MongoConnection, MongoDriver}
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Examples {

  def main(args: Array[String]) {
    val driver = new MongoDriver()
    val connection: MongoConnection = MongoConnection.parseURI("mongodb://oplogger:oplogger@cockney.4.mongolayer.com:10122,cockney.5.mongolayer.com:10122/logs?replicaSet=set-56af4ad6aaeb0dd3a200154b").map { parsed =>
      driver.connection(parsed)
    } match {
      case Success(conn) => conn
      case Failure(ex) => throw new RuntimeException(s"Could not establish the connection: $ex")
    }

    val db = connection("local")
    val collection = db[BSONCollection]("oplog.rs")

    val enumerator: Enumerator[BSONDocument] = collection
      .find(BSONDocument())
      .options(QueryOpts().tailable.awaitData)
      .cursor[BSONDocument]().enumerate()
    val process: Iteratee[BSONDocument, Unit] = Iteratee.foreach { result =>
      println(result.getAs[BSONDocument]("o").map(_.get("message")))
    }

    enumerator.run(process)
  }

}
