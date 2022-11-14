package Presentation

import akka.http.scaladsl.server.Directives._


class RouteRoot(helloPresentation: HelloPresentation) {

  def route() = {
    concat(
      helloPresentation.route
    )
  }

}
