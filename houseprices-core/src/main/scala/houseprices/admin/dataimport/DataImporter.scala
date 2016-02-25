package houseprices.admin.dataimport

import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.actorRef2Scala
import houseprices.elasticsearch.BulkAddPricePaid
import org.elasticsearch.client.Client
import java.nio.file.Paths

case class ImportData(fileName: String)

class DataImporter(dataFolder: String, bulkAddFactory: String => BulkAddPricePaid) extends Actor with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  def receive = {
    case ImportData(csvFile) =>
      log.info("import request for file: {}", csvFile)
      val bulkAdd = bulkAddFactory(makeFullPath(csvFile))
      bulkAdd.run
    case _@ msg => log.warning("Unsupported message, {}")
  }

  def makeFullPath(fileName: String) = Paths.get(dataFolder, fileName).toAbsolutePath().toString()

}