package cz.boris.event

import cz.boris.event.web.Server

object ApplicationMain {
  // upload log file -> Source to Sink
  // parse file - map to case class - persist in mongo db - Flow
  // index data persisted in mongo - Source to Sink
  // REST endpoint to GET data from mongo - Source to Sink
  // search via ES - Source to Sink

  // REST -> Mongo -> ES -> REST
  def main(args: Array[String]) {
    new Server().run()

  }
}