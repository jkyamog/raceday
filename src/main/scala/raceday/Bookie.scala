package raceday

import akka.actor.Actor
import akka.stm._
import collection.mutable.ListBuffer

class Bookie extends Actor {
  val bets = ListBuffer[Bet] ()

  def receive = {
    case bet: Bet => {
      println("got bet " + bet)
      bets += bet
      self.channel ! new Ticket(bet.amount, bet.raceNumber)
    }
    case ticket: Ticket => {
      println("got ticket " + ticket.ticketNo + " " + ticket)
      if (ticket.raceNumber == RaceDay.getWinner) {
        val winnings = calcWinnings
        if (Book.debit(ticket.amount)) {
          self.channel ! new WinningReceipt(ticket, winnings)
          println("ticket won " + ticket.ticketNo)
        } else become(inHiding)
      }
    }
  }

  def calcWinnings: Double = {
    val totalAmount = bets map (_.amount) sum //foldLeft(0.00) {(acc, bet) => acc + bet.amount}

    totalAmount / bets.size
  }

  def inHiding: Receive = {
    case _ => println("go away, i am not here")
  }
}

case class Bet(amount: Double, raceNumber: Int)

object Book {
  val money = Ref(1000.00)
  val bankrupt = Ref(false)

  def credit(amount: Double) {
    atomic {
      money alter (_ + amount)
    }
  }

  def debit(amount: Double): Boolean = {
    atomic {
      if (money.get > 0) {
        money alter (_ - amount)
        true
      } else {
        bankrupt set true
        println("Bankrupt!!!!!")
        false
      }
    }
  }

}