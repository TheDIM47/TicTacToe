package services

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import services.GameServer.{EnterGame, ExitGame, Step}

class T3User(server: ActorRef, out: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case s: String => if (sender == server) out ! s
    else {
      try {
        val n = s.toInt
        server ! Step(n)
        log.info(s"Do Step($n)")
      } catch {
        case e: Throwable => sender ! s"Invalid message $s. Integer value expected"
      }
    }
    case x => log.error(s"Invalid message type $x")
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    server ! EnterGame(self)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    server ! ExitGame(self)
  }
}

object T3User {
  def props(server: ActorRef, out: ActorRef): Props = Props(classOf[T3User], server, out)
}