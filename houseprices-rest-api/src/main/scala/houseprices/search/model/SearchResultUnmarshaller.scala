package houseprices.search.model
import spray.json.DefaultJsonProtocol
import spray.json.JsArray
import spray.json.RootJsonFormat
import spray.json.JsString
import spray.json.JsNumber
import spray.json._
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.model.HttpEntity
import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal

object SearchResultUnmarshaller {

  import SearchResultJsonProtocol._
  implicit val mat = akka.stream.ActorMaterializer

  implicit val um: Unmarshaller[HttpEntity, SearchResult] = {
    Unmarshaller.stringUnmarshaller.map { payload =>
      JsonParser(payload).convertTo[SearchResult]
    }
  }

}