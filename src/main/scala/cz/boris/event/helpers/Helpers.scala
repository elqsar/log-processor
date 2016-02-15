package cz.boris.event.helpers

object Helpers {

  implicit class StrOps(value: String) {
    def toClassName: String = {
      value.substring(value.lastIndexOf(".") + 1)
    }
    def toCleanMessage: String = {
      value.replace("- ", "")
    }
  }

}
