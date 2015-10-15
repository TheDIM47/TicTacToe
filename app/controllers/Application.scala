package controllers

import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller, WebSocket}
import play.libs.Akka
import services._

object Application extends Controller {
  import play.api.Logger
  import play.api.Play.current

  // create stock server
  val gameServer = Akka.system.actorOf(GameServer.props)

  def index = Action {
    Ok("Welcome to simple Scala/Play/WebSocket TicTacToe Game")
  }

  // TicTacToe service handler
  def t3ws = WebSocket.acceptWithActor[String, String] { request =>
    out => T3User.props(gameServer, out)
  }

}
