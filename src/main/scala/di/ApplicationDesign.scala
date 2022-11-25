package di

import application.{BettingTicketRegisterApplication, BettingTicketRegisterApplicationImpl, IdToken}
import wvlet.airframe.newDesign

object ApplicationDesign extends ApplicationDesign

trait ApplicationDesign {
  val design = newDesign
    .bind[BettingTicketRegisterApplication].to[BettingTicketRegisterApplicationImpl]
    .bind[IdToken].toSingleton
}
