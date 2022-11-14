package presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import application.BettingTicketRegisterApplication
import model.BettingTicketPayload

class BettingTicketRegisterPresentation(implicit system: ActorSystem, bettingTicketRegisterApplication: BettingTicketRegisterApplication) {

  def route: Route = {
    path("bettingTicketRegister" / Segment) { payload: String =>
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, bettingTicketRegisterApplication.registerBettingTicket(BettingTicketPayload.apply(payload)).toString))
      }
    }
  }
}
