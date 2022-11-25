package di

import presentation.{AuthorizationHeaderDirective, BettingTicketRegisterPresentation, HealthCheck, RouteRoot}
import wvlet.airframe.newDesign

object PresentationDesign extends PresentationDesign

trait PresentationDesign {

  val design = newDesign
    .bind[BettingTicketRegisterPresentation].toSingleton
    .bind[AuthorizationHeaderDirective].toSingleton
    .bind[HealthCheck].toSingleton
    .bind[RouteRoot].toSingleton

}
