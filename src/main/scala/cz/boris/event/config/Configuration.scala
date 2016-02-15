package cz.boris.event.config

import com.typesafe.config.ConfigFactory

object Configuration {
  val config = ConfigFactory.load()

  val port = config.getInt("server.port")
  val host = config.getString("server.host")

  val dbHost = config.getString("database.url")
  val dbPort = config.getInt("database.port")

  val logPattern = config.getString("processor.log-pattern")

}
