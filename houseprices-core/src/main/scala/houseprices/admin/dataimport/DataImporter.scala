package houseprices.admin.dataimport

import java.util.concurrent.Executor

import scala.concurrent.ExecutionContext

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.actorRef2Scala

case class ImportData(fileName: String)

class DataImporter(dataFolder: String) extends Actor with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  def receive = {
    case ImportData(fileName) => sender ! "TODO"

  }

}