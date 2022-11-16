package model

case class NormalWinBettingTicketsInfo(bettingTicketSelection: BettingTicketSelection, hoursNumber: HoursNumber, cost: Int) {
  override def toString: String = {
    bettingTicketSelection.toString + "," + hoursNumber.value + "番" + cost + "円"
  }
}
