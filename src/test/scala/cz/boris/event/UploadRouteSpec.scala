package cz.boris.event

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import cz.boris.event.web.routes.UploadRoute
import org.scalatest.{Matchers, WordSpec}

class UploadRouteSpec extends WordSpec
with Matchers
with Directives
with ScalatestRouteTest
with UploadRoute {
  "Log file" should {
    "be uploaded" in {
      val fileUpload = Multipart.FormData(
        Multipart.FormData.BodyPart.Strict(
          "logs",
          HttpEntity("log content"),
          Map("filename" ->
            """
              |19:53:32.377 [RGT-system-akka.actor.default-dispatcher-74] INFO  com.gorkana.actors.processor.Filter - Inactive contact rejected: 1330779.Status: 2
            """.stripMargin)))

      Post("/uploads", fileUpload) ~> upload ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Upload success"
      }
    }
  }
}
