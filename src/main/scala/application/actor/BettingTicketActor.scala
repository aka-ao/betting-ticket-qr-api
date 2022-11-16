package application.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import model.NormalWinBettingTicketPayload
import org.slf4j.LoggerFactory

object BettingTicketActor {

  val log = LoggerFactory.getLogger(this.getClass)

  sealed trait Command
  case class BettingInfo(normalWinBettingTicketPayload: NormalWinBettingTicketPayload) extends Command
  case class GetBettingInfo(replyTo: ActorRef[Command]) extends Command
  case class ResBettingInfo(payload: Option[NormalWinBettingTicketPayload]) extends Command

  sealed trait Event
  case class Saved(normalWinBettingTicketPayload: NormalWinBettingTicketPayload) extends Event

  final case class State(value: Option[NormalWinBettingTicketPayload])

  def apply(id: PersistenceId): Behavior[Command] = {
    EventSourcedBehavior[Command, Event, State](
      persistenceId = id,
      emptyState = State(None),
      commandHandler = commandHandler,
      eventHandler = eventHandler
    )
  }

  private def commandHandler(state: State, command: Command): Effect[Event, State] = {
    command match {
      case b: BettingInfo => {
        log.info("receive betting ticket info")
        log.info(b.normalWinBettingTicketPayload.toString)
        Effect
          .persist(Saved(b.normalWinBettingTicketPayload))
          .thenRun { state =>
            println(state.value)
          }
      }
      case g: GetBettingInfo =>
        Effect
          .reply(g.replyTo)(ResBettingInfo(state.value))
      case _ =>
        Effect.unstashAll()
    }
  }

  private def eventHandler(state: State, event: Event): State = {
    event match {
      case s: Saved =>
        State(Some(s.normalWinBettingTicketPayload))
    }
  }
}
