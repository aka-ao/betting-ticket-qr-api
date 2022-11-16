package presentation

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import application.BettingTicketRegisterApplication
import model.BettingTicketPayload
import org.slf4j.LoggerFactory

class BettingTicketRegisterPresentation(bettingTicketRegisterApplication: BettingTicketRegisterApplication) {

  val log = LoggerFactory.getLogger(this.getClass)

  def route: Route = {
    path("bettingTicketRegister" / Segment) { payload: String =>
      get {

        val bettingTicketPayload = BettingTicketPayload.apply(payload)
        val res = bettingTicketRegisterApplication.registerBettingTicket(bettingTicketPayload)
        val responseHtml = StringBuilder.newBuilder
          .append("<p>").append(bettingTicketPayload.toString).append("</p>")
          .append("<p>").append(res.mkString("<br>")).append("</p>")

        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,responseHtml.result()))
      }
    }
  }
}
