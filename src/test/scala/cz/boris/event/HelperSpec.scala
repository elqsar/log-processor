package cz.boris.event

import org.scalatest.{Matchers, FlatSpecLike}
import cz.boris.event.helpers.Helpers._

class HelperSpec extends FlatSpecLike with Matchers {

  "toClassName helper" should "extract class name properly" in {
    val className = "org.test.Processor"
    className.toClassName should be ("Processor")
  }

  "toCleanMessage helper" should "extract message properly" in {
    val message = "- Some helpful message"
    message.toCleanMessage should be ("Some helpful message")
  }

}
