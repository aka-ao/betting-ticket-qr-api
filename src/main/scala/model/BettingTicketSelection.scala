package model

case class BettingTicketSelection(value: Int) {
  override def toString: String = {
    value match {
      case 1 => "単勝"
      case 2 => "複勝"
      case 3 => "枠連"
      case 5 => "馬連"
      case 6 => "馬単"
      case 7 => "ワイド"
      case 8 => "3連複"
      case 9 => "3連単"
      case _ => "その他"
    }
  }
}
