package raceday

import akka.actor.{Scheduler, Actor}
import util.Random
import java.util.concurrent.TimeUnit.SECONDS

class Horse(jockeyName: String, raceNumber: Int) extends Actor {
  private var distanceRan = 0;
  private val speed = Random

  def receive = {
    case "go" => {
      println("starting to run " + jockeyName + " " + raceNumber)
      Scheduler.scheduleOnce(self, "run", 1, SECONDS)
      become(running)
    }
    case _ => println("unsupported action")
  }

  def running: Receive = {
    case "run" => {
      distanceRan += speed.nextInt(20)
      println(jockeyName + " " + raceNumber + " running for " + distanceRan + "meters")
      if (distanceRan >= 100) {
        println("done running " + jockeyName + " " + raceNumber)
        RaceDay.setWinner(raceNumber)
        become(stopped)
      } else
        Scheduler.scheduleOnce(self, "run", 1, SECONDS)
    }
    case _ => println("currently running and covered " + distanceRan + " meters")
  }

  def stopped: Receive = {
    case _ => println("finished running");
  }
}

object Horse {
  def apply(jockeyName: String, raceNumber: Int) = new Horse(jockeyName, raceNumber)
}