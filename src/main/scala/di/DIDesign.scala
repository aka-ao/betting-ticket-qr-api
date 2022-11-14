package di

import akka.actor.ActorSystem
import wvlet.airframe.newDesign

object DIDesign extends DIDesign


trait DIDesign {

  def design(system: ActorSystem) =newDesign
    .bind[ActorSystem].toInstance(system)
    .add(PresentationDesign.design)

}
