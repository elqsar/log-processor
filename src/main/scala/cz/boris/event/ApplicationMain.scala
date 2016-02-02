package cz.boris.event

import cz.boris.event.web.Server

object ApplicationMain {
  def main(args: Array[String]) {
    new Server().run()
  }
}