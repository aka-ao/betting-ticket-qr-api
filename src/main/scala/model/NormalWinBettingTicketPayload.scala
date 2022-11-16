package model

/**
 * 買目通常・式別単勝または複勝の場合のPayload
 * */
case class NormalWinBettingTicketPayload(payload: BettingTicketPayload) extends AdditionalBettingTicketPayload {

  private def getBettingTicketSelection(index: Int) = {
    BettingTicketSelection.apply(payload.value.substring(index,index + 1).toInt)
  }

  private def getBettingHorse(index: Int) = {
    HoursNumber.apply(payload.value.substring(index + 1, index + 3).toInt)
  }

  private def getBettingCost(index: Int) = {
    payload.value.substring(index+3,index+10).toInt * 100
  }

  def getWinBettingTicketInfo= {
    val startIndex = 42
    var endIndex = 0

    val bettingTicketInfo = List.range(0,4) map (i => {
      val index = startIndex + i * 10
      val bettingSelection = getBettingTicketSelection(index)

      // mapはBreakできないのでその他のIndexを記憶しておく
      if (bettingSelection.value == 0 && endIndex == 0) {
        endIndex = i
      }

      val horse = getBettingHorse(index)
      val cost = getBettingCost(index)

      NormalWinBettingTicketsInfo.apply(bettingSelection, horse, cost)
    })

    // その他より前の要素の配列のみを取り出す
    bettingTicketInfo.slice(0, endIndex)
  }

}
