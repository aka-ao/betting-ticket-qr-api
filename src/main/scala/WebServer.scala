import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.stream.ActorMaterializer
import com.github.blemale.scaffeine.{AsyncLoadingCache, Scaffeine}
import com.typesafe.config.{Config, ConfigException, ConfigFactory}

import scala.concurrent.Future
import scala.io.StdIn

object WebServer {
  def main(args: Array[String]) {

    final case class User(userName: String, int: Int)

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    val executor = system.dispatcher

    val cache: AsyncLoadingCache[User, Int] = Scaffeine()
      .recordStats()
      .expireAfterWrite(20, TimeUnit.SECONDS)
      .executor(executor)
      .buildAsyncFuture((user: User) => {
        println(s"no cache: ${DateTime.now.toString()}")
        Thread.sleep(3000)
        Future.successful(user.int)
      })


    val route =
      pathPrefix("hello") {
        concat(
          pathEnd{
            get {
              extractHost { hostName =>
                system.log.info(hostName)
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
              }
            }
          },
          path(IntNumber) { int =>
            authenticateBasic(realm = "secure site", myUserPassAuthenticator) { userName =>
              val user = User.apply(userName, int)
              complete(cache.get(user).map { id =>
                HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Say hello to $userName $id </h1>")
              })
            }
          }
        )
      }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

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