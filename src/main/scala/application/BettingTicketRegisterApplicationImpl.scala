package application

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.persistence.typed.PersistenceId
import akka.util.Timeout
import application.actor.BettingTicketActor
import model.{BettingTicketPayload, NormalWinBettingTicketPayload, NormalWinBettingTicketsInfo}
import org.slf4j.LoggerFactory

import scala.concurrent.duration.DurationInt

class BettingTicketRegisterApplicationImpl extends BettingTicketRegisterApplication {
  implicit val askTimeout: Timeout = 3.seconds

  override def registerBettingTicket(payload: BettingTicketPayload): List[NormalWinBettingTicketsInfo] = {


    import BettingTicketActor._

    val behavior: Behavior[BettingTicketActor.Command] =
      BettingTicketActor.apply(PersistenceId.ofUniqueId(payload.getBettingTicketNo))

    implicit val actor: ActorSystem[BettingTicketActor.Command] =
      ActorSystem(behavior, "bettingTicketActor")

    log.info("start BettingTicketActor")

    import actor.executionContext

    val actorRef: ActorRef[BettingTicketActor.Command] = actor

    actor ! BettingInfo(NormalWinBettingTicketPayload(payload))
    val response = actorRef.ask(replyTo => GetBettingInfo(replyTo))
    response.map{ res =>
      log.info(res.toString)
    }
    val bettingInfo = NormalWinBettingTicketPayload(payload).getWinBettingTicketInfo
    bettingInfo
  }
}
