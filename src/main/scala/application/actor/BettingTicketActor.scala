package application.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import model.NormalWinBettingTicketPayload
import org.slf4j.LoggerFactory

object BettingTicketActor {

  private val log = LoggerFactory.getLogger(this.getClass)

  sealed trait Command
  case class BettingInfo(normalWinBettingTicketPayload: NormalWinBettingTicketPayload) extends Command
  case class GetBettingInfo(replyTo: ActorRef[Command]) extends Command
  case class ResBettingInfo(payload: Option[NormalWinBettingTicketPayload]) extends Command

  sealed trait Event
  case class Saved(normalWinBettingTicketPayload: NormalWinBettingTicketPayload) extends Event

  sealed trait State {
    def applyCommand(command: Command): Effect[Event, State]
    def applyEvent(event: Event): State
  }

  final case class InitState(value: Option[NormalWinBettingTicketPayload]) extends State {
    override def applyCommand(command: Command): Effect[Event, State] = commandHandler(value, command)

    override def applyEvent(event: Event): State = eventHandler(event)

    private def commandHandler(state: Option[NormalWinBettingTicketPayload], command: Command): Effect[Event, State] = {
      log.info("this status is InitState")
      command match {
        case b: BettingInfo => {
          log.info("receive betting ticket info")
          log.info(b.normalWinBettingTicketPayload.toString)
          Effect
            .persist(Saved(b.normalWinBettingTicketPayload))
        }
        case _ =>
          Effect.unstashAll()
      }
    }

    private def eventHandler(event: Event): State = {
      event match {
        case s: Saved => Registered(Some(s.normalWinBettingTicketPayload))
        case _ => throw new IllegalArgumentException()
      }
    }
  }

  final case class Registered(value: Option[NormalWinBettingTicketPayload]) extends State {
    override def applyCommand(command: Command): Effect[Event, State] = {
      log.info("this status is Registered")
      command match {
        case g: GetBettingInfo =>
          Effect.reply(g.replyTo)(ResBettingInfo(value))
        case _ =>
          Effect.unstashAll()
      }
    }

    override def applyEvent(event: Event): State = Registered(value)
  }

  def apply(id: PersistenceId): Behavior[Command] =
    EventSourcedBehavior[Command, Event, State](
      persistenceId = id,
      emptyState = InitState(None),
      commandHandler = (state, command) => state.applyCommand(command),
      eventHandler = (state, event) => state.applyEvent(event)
    )
}
