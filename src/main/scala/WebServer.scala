import Presentation.RouteRoot
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.Credentials
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import di.DIDesign

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer {
  def main(args: Array[String]) {

    final case class User(userName: String, int: Int, unit: Unit)

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val d = DIDesign.design(system)
    val session = d.newSessionBuilder.create
    session.start
    
    val bindingFuture = Http().bindAndHandle(session.build[RouteRoot].route(), "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def myUserPassAuthenticator(credentials: Credentials): Option[String] = {
    val config: Config = ConfigFactory.load()
    credentials match {
      case p @ Credentials.Provided(id) =>
        try {
          val password = config.getString("basicAuth.user." + id + ".password")
          if (p.verify(password)) Some(id)
          else None
        } catch {
          case _: ConfigException.Missing => None
          case _: ConfigException.WrongType => None
        }
      case _ => None
    }
  }

}