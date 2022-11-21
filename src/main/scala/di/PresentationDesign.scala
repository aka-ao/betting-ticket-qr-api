package di

import presentation.{BettingTicketRegisterPresentation, HealthCheck, RouteRoot}
import wvlet.airframe.{bind, newDesign}

object PresentationDesign extends PresentationDesign

trait PresentationDesign {

  val design = newDesign
    .bind[BettingTicketRegisterPresentation].toSingleton
    .bind[HealthCheck].toSingleton
    .bind[RouteRoot].toSingleton

}
