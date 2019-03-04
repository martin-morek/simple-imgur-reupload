package commons

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Reads, __}

trait JsonFormats {

  implicit val urlUploadRequestReads: Reads[UrlUploadRequest] =
    (__ \ "urls").read[List[String]].map(UrlUploadRequest(_))

  implicit val errorDataReads: Reads[ErrorResponseData] = (
    (__ \ "error").read[String] and
      (__ \ "request").read[String] and
      (__ \ "method").read[String]
  )(ErrorResponseData.apply _)

  implicit val errorResponseReads: Reads[ErrorResponse] = (
    (__ \ "data").read[ErrorResponseData] and
      (__ \ "success").read[Boolean] and
      (__ \ "status").read[Int]
  )(ErrorResponse.apply _)

}
