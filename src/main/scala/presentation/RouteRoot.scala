package presentation

import akka.http.scaladsl.server.Directives._


class RouteRoot(helloPresentation: BettingTicketRegisterPresentation) {

  def route() = {
    concat(
      helloPresentation.route
    )
  }

}
