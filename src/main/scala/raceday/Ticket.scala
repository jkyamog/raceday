package raceday

import util.Random

case class Ticket(amount: Double, raceNumber: Int) {
  val ticketNo = Random.nextInt(1000)
}