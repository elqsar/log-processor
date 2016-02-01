package cz.boris.event.service

trait FileValidator {

  def isLogFile(name: String): Boolean = name.substring(name.lastIndexOf(".") + 1) == "log"

}
