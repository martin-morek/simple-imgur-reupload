package actors

import actors.ReUploader.ReUploadMessage
import akka.actor.{Actor, ActorLogging}
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import commons.{ErrorResponse, JsonFormats}
import play.api.libs.json.JsSuccess
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ReUploader {
  case class ReUploadMessage(urls: List[String])
}

class ReUploader(ws: WSClient, config: Config)(implicit val mat: ActorMaterializer)
  extends Actor with ActorLogging with JsonFormats{
  val UploadUrl = "https://api.imgur.com/3/image"
  val ClientId = config.getString("imgur.clientId")

  override def receive: Receive = {
    case ReUploadMessage(urls) => reUpload(urls)
  }

  def reUpload(urls: List[String]) = {
    urls.distinct.map { url =>

      val futureResponse: Future[WSResponse] =
        ws.url(url).withMethod("GET").stream()

      futureResponse.flatMap { res =>
        ws.url(UploadUrl)
          .addHttpHeaders("Authorization" -> s"Client-ID ${ClientId}",
            "Content-Type" -> "application/x-www-form-urlencoded")
          .withBody(res.bodyAsSource)
          .execute("POST")

          .map(r => r.status match {
            case 200 =>
              log.info("Image new url: {}", r.json \"data\" \"link\").as[String])
            case _ =>
              r.json.validate[ErrorResponse] match {
                case JsSuccess(json, _) => log.error("Reason: {}.",  json.data.error)
                case _ => log.error("Reason: Response cannot be parsed.")
              }
          })
      }
    }
  }
}