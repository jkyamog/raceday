package raceday

import collection.mutable.ListBuffer
import util.Random
import akka.actor.{ActorRef, Actor}

class Punter extends Actor {
  private val tickets = ListBuffer[Ticket] ()

  def receive = {
    case ticket: Ticket => {
      println("got ticket: " + ticket + " " + ticket.ticketNo)
      tickets += ticket
    }
    case punters: ActorRef => {
      if (RaceDay.getWinner() > 0) {
        println("checking tickets")
        tickets.foreach((t: Ticket) => {
          punters ! t
        })
      } else {
        println("punting")
        punters ! new Bet(Random.nextInt(500), Random.nextInt(4))
      }
    }
    case winningReceipt: WinningReceipt => {
      println("i won! " + winningReceipt.winnings + " for ticket " + winningReceipt.ticket.ticketNo)
    }
    case _ => println("not supported")
  }

}