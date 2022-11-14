package application

import akka.actor.ActorSystem
import model.BettingTicketPayload

trait BettingTicketRegisterApplication {
  def registerBettingTicket(payload: BettingTicketPayload)(implicit system: ActorSystem): Boolean
}
