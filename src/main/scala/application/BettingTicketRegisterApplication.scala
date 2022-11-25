package application

import model.{BettingTicketPayload, NormalWinBettingTicketsInfo}

import scala.concurrent.Future

trait BettingTicketRegisterApplication {
  def registerBettingTicket(payload: BettingTicketPayload, id: String): Future[List[NormalWinBettingTicketsInfo]]
}
