package presentation

import akka.http.scaladsl.server.Directives._


class RouteRoot(helloPresentation: BettingTicketRegisterPresentation, healthCheck: HealthCheck) {

  def route() = {
    concat(
      helloPresentation.route,
      healthCheck.route
    )
  }

}
