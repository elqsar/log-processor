package cz.boris.event.model

case class LogEvent(
                     time: String,
                     thread: String,
                     mode: String,
                     className: String,
                     message: String)
