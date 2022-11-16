package application

import model.{BettingTicketPayload, NormalWinBettingTicketsInfo}

trait BettingTicketRegisterApplication {
  def registerBettingTicket(payload: BettingTicketPayload): List[NormalWinBettingTicketsInfo]
}
