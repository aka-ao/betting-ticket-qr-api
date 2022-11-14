package model

case class WinBettingTicketPayload(payload: BettingTicketPayload) {

  private def getBettingTicketSelection(index: Int) = {
    payload.value.substring(index,index + 1) match {
      case "1" => "単勝"
      case "2" => "複勝"
      case "3" => "枠連"
      case "5" => "馬連"
      case "6" => "馬単"
      case "7" => "ワイド"
      case "8" => "3連複"
      case "9" => "3連単"
      case _ => "その他"
    }
  }

  private def getBettingHorse(index: Int) = {
    payload.value.substring(index + 1, index + 3).toInt
  }

  private def getBettingCost(index: Int) = {
    payload.value.substring(index+3,index+10).toInt * 100
  }

  def getWinBettingTicketInfo: List[(String, Int, Int)] = {
    val startIndex = 42
    var endIndex = 0

    val bettingTicketInfo = List.range(0,4) map (i => {
      val index = startIndex + i * 10
      val bettingSelection = getBettingTicketSelection(index)

      // mapはBreakできないのでその他のIndexを記憶しておく
      if (bettingSelection.equals("その他") && endIndex == 0) {
        endIndex = i
      }

      val horse = getBettingHorse(index)
      val cost = getBettingCost(index)

      (bettingSelection, horse, cost)
    })

    // その他より前の要素の配列のみを取り出す
    bettingTicketInfo.slice(0, endIndex)
  }



}
