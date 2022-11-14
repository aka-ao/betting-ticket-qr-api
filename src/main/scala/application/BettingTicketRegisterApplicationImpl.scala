package application

import akka.actor.ActorSystem
import model.{BettingTicketPayload, WinBettingTicketPayload}

class BettingTicketRegisterApplicationImpl extends BettingTicketRegisterApplication {
  override def registerBettingTicket(payload: BettingTicketPayload)(implicit system: ActorSystem): Boolean = {
    system.log.info(payload.toString)
    system.log.info(WinBettingTicketPayload.apply(payload).getWinBettingTicketInfo.mkString("-"));
    true
  }
}
