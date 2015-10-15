package services

import akka.actor._
import model.{Mark, Board}
import services.GameServer._

class GameServer extends Actor with ActorLogging {
  var user1 : Option[ActorRef] = None // X
  var user2 : Option[ActorRef] = None // O
  var board : Option[Board] = None

  override def receive: Receive = {
    case EnterGame(_) =>
      if (user1.isEmpty) {
        context.watch(sender)
        user1 = Some(sender)
        sendMessage(user1, s"You are added to game as Player 1 (X)")
        log.info(s"Player $sender entered game as user 1 (X)")
      } else if (user2.isEmpty) {
        context.watch(sender)
        user2 = Some(sender)
        sendMessage(user2, s"You are added to game as Player 2 (O)")
        log.info(s"Player $sender entered game as user 2 (O)")
      } else {
        sender ! s"No more free games! Please, try later."
      }
      // start game when users completed
      if (usersDefined) {
        board = Some(new Board(3))
        broadcastMessage(board.get.toBoardString)
        sendMessage(user1, board.get.toFreeString)
        sendMessage(user1, s"Let's start! Enter your move (X)")
        sendMessage(user2, s"Please, wait.")
      }
    case Step(n) =>
      if (usersDefined) {
        if (sender == user1.get) {
          if (board.get.step(n, Mark.X)) checkBoard else sender ! s"Invalid move X($n)"
        } else if (sender == user2.get) {
          if (board.get.step(n, Mark.O)) checkBoard else sender ! s"Invalid move O($n)"
        } else {
          sender ! s"Invalid move ($n) by $sender"
        }
      } else sender ! s"Players not completed. Please, wait!"
    case ExitGame(a) =>
      if (user1.isDefined && user1.get == a) {
        user1 = None
        board = None
        sendMessage(user2, "Player 1 leave game. Game will be restarted")
      } else if (user2.isDefined && user2.get == a) {
        user2 = None
        board = None
        sendMessage(user1, "Player 2 leave game. Game will be restarted")
      }
      log.info(s"Player $sender leaving game")
    case Terminated =>
      log.info(s"Player $sender terminated and leaving game")
      context.unwatch(sender)
      self ! ExitGame(sender)
    case x =>
      log.info(s"Unknown message $x from $sender")
  }

  def checkBoard: Unit = {
    if (board.get.isFull) {
      broadcastMessage(s"Borad completed! Game will be restarted")
    } else {
      val m = board.get.isWin
      if (m != Mark.Empty) {
        broadcastMessage(board.get.toBoardString)
        broadcastMessage(s"GameOver! $m wins. Game will be restarted")
      } else {
        broadcastMessage(board.get.toBoardString)
        val next = if (board.get.expectedMark == Mark.X) user1 else user2
        sendMessage(next, board.get.toFreeString)
        sendMessage(next, "Please, enter your move:")
      }
    }
  }

  def usersDefined = user1.isDefined && user2.isDefined

  def sendMessage(u: Option[ActorRef], s: String) = u.map(x => x ! s)

  def broadcastMessage(s: String) = {
    user1.map(x => x ! s)
    user2.map(x => x ! s)
  }
}


object GameServer {

  case class EnterGame(actor: ActorRef)

  case class ExitGame(actor: ActorRef)

  case class Step(s: Int)

  def props: Props = Props(classOf[GameServer])
}
