package presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import application.BettingTicketRegisterApplication
import model.BettingTicketPayload
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContextExecutor

class BettingTicketRegisterPresentation(bettingTicketRegisterApplication: BettingTicketRegisterApplication, system: ActorSystem, auth: AuthorizationHeaderDirective) {

  val log: Logger = LoggerFactory.getLogger(this.getClass)
  implicit val ec: ExecutionContextExecutor = system.dispatcher


  def route(): Route = {
    path("bettingTicketRegister" / Segment) { payload: String =>
      auth.authorize() { id =>
        get {
          val bettingTicketPayload = BettingTicketPayload.apply(payload)
          val res = bettingTicketRegisterApplication.registerBettingTicket(bettingTicketPayload, id)
          onSuccess(res) {
            r => {
              val responseHtml = StringBuilder.newBuilder
                .append("<p>").append(bettingTicketPayload.toString).append("</p>")
                .append("<p>").append(r.mkString("<br>")).append("</p>")

              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, responseHtml.result()))
            }
          }
        }
      }
    }
  }
}
