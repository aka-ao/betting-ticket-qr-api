package presentation

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.authenticateOAuth2Async
import akka.http.scaladsl.server.directives.BasicDirectives.extractExecutionContext
import akka.http.scaladsl.server.directives.Credentials
import application.IdToken
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

class AuthorizationHeaderDirective(idToken: IdToken) {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  private def authenticate(credentials: Credentials)(
    implicit ec: ExecutionContext,
  ): Future[Option[String]] = {
    credentials match {
      case Credentials.Provided(token) =>
        log.info("token:" + token)
        val id = idToken.verify(token).getSubject
        log.info("request user: " + id)
        Future.successful(Some(id))
      case Credentials.Missing =>
        Future.successful(None)
    }
  }

  def authorize(): Directive1[String] = {
    extractExecutionContext
      .flatMap { implicit ec =>
        authenticateOAuth2Async("", authenticate(_))
      }
  }
}
