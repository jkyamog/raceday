package raceday

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import akka.actor.Actor._

class BookieTest extends JUnitSuite {

  @Test
  def testBookie {
    val bookie = actorOf[Bookie].start()
    val punter = actorOf[Punter].start()

    val ticket = bookie !! new Bet(10.0, 1)
    ticket match {
      case Some(ticket: Ticket) => {
        punter ! ticket

        RaceDay.setWinner(1)

        bookie !! ticket

        println(ticket)
      }
      case _ => println("something went wrong")
    }
  }

}