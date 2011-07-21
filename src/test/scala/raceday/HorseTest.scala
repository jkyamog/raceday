package raceday

import akka.actor.Actor._
import org.scalatest.junit.JUnitSuite
import org.junit.Test

class HorseTest extends JUnitSuite {

  @Test
  def testHorses {
    val horses = actorOf(Horse("jun", 1)).start() ::
      actorOf(Horse("grant", 2)).start() ::
      actorOf(Horse("tiger", 3)).start() ::
      actorOf(Horse("liv", 4)).start() :: Nil

    horses.foreach(_ ! "go")

    assert(true)
  }
}
