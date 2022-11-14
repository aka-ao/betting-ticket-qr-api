package presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class HelloPresentation(system: ActorSystem) {

  def route: Route = {
    pathPrefix("hello") {
      concat(
        pathEnd {
          get {
            entity(as[String]) { json =>
              system.log.info(json)
              extractHost { hostName =>
                system.log.info(hostName)
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
              }
            }
          }
        },
      )
    }
    ,
  }
}
