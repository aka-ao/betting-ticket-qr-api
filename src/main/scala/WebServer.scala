import presentation.RouteRoot
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import di.DIDesign

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer {
  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val d = DIDesign.design(system)
    val session = d.newSessionBuilder.create
    session.start
    
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(session.build[RouteRoot].route())

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}