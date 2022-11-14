package di

import presentation.{BettingTicketRegisterPresentation, RouteRoot}
import wvlet.airframe.newDesign

object PresentationDesign extends PresentationDesign

trait PresentationDesign {

  val design = newDesign
    .bind[BettingTicketRegisterPresentation].toSingleton
    .bind[RouteRoot].toSingleton

}
