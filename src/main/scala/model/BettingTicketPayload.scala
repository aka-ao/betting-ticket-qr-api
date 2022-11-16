package model

case class BettingTicketPayload(value: String) extends AnyVal {
  override def toString: String = {
    return getArea + getYear + "年" + getTimes + "回" + getDays + "日" + getRace + "レース " + getBettingType
  }

  def getArea = {
    value.substring(1,3) match {
      case "01" => "札幌"
      case "02" => "函館"
      case "03" => "福島"
      case "04" => "新潟"
      case "05" => "東京"
      case "06" => "中山"
      case "07" => "中京"
      case "08" => "京都"
      case "09" => "阪神"
      case "10" => "小倉"
    }
  }

  def getYear = {
    value.substring(6,8).toInt + 2000
  }

  def getTimes = {
    value.substring(8,10).toInt
  }

  def getDays = {
    value.substring(10,12).toInt
  }

  def getRace = {
    value.substring(12,14).toInt
  }

  def getBettingType = {
    value.substring(14,15) match {
      case "0" => "通常"
      case "1" => "ボックス"
      case "2" => "ながし"
      case "3" => "フォーメーション"
      case "4" => "クイックピック"
      case "5" => "応援馬券"
    }
  }

  def getBettingTicketNo = {
    value.substring(16,42)
  }
}
