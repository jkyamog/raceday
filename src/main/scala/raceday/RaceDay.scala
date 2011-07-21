package raceday

import akka.actor.Actor._
import akka.routing.Routing._
import akka.routing.CyclicIterator
import akka.actor.Scheduler
import akka.stm._
import java.util.concurrent.TimeUnit.SECONDS

object RaceDay {
  private val winner = Ref(0)
  val horses = actorOf(Horse("jun", 1)).start() ::
    actorOf(Horse("grant", 2)).start() ::
    actorOf(Horse("tiger", 3)).start() ::
    actorOf(Horse("liv", 4)).start() :: Nil

  val miroslav = actorOf[Bookie].start()
  val loren = actorOf[Bookie].start()
  val bookies = loadBalancerActor(new CyclicIterator(List(miroslav, loren)))

  val punters = for (i <- 1 to 10) yield actorOf[Punter].start()

  def main(args: Array[String]) {

    horses.foreach(_ ! "go")

    punters.foreach(Scheduler.schedule(_, bookies, 0, 10, SECONDS))

  }

  def setWinner(raceNumber: Int) {
    atomic {
      if (getWinner() == 0) {
        println("************ setting winner " + raceNumber)
        winner.set(raceNumber)
      }
    }
  }

  def getWinner(): Int = atomic {
    winner.get
  }

}