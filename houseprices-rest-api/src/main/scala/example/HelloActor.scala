package example

import akka.actor.Actor

class HelloActor extends Actor {
  
  def receive = {
    case msg: String => {
      sender ! "Hello " + msg
    }
  }
}