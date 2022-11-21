package presentation

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class HealthCheck {

  def route: Route = path("healthCheck") {
    complete(StatusCodes.OK)
  }
}
