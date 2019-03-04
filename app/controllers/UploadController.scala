package controllers

import java.util.UUID

import actors.ReUploader
import actors.ReUploader.ReUploadMessage
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import com.google.inject.Inject
import com.typesafe.config.Config
import commons.{JsonFormats, UrlUploadRequest}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class UploadController @Inject()(
    ws: WSClient,
    config: Config,
    cc: ControllerComponents)(implicit mat: Materializer, ec: ExecutionContext)
    extends AbstractController(cc) with JsonFormats {

  val system = ActorSystem("ReUploadSystem")

  def upload = Action { implicit request =>
    val jobId = UUID.randomUUID()

    request.body.asJson.map(json =>
      json.validate[UrlUploadRequest] match {
        case JsSuccess(js, _) => {
          val actorInstance = system.actorOf(Props(classOf[ReUploader], ws, config, mat), name = jobId.toString)

          actorInstance ! ReUploadMessage(js.urls.distinct)
        }
        case _ => Logger.error(s"Job ID:${jobId} failed. Reason: Cannot parse request")
      }
    )
    Ok(Json.obj("jobId" -> jobId))
  }
}
