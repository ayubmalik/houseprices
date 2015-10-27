package houseprices.elasticsearch

import java.nio.file.Files
import java.nio.file.Paths
import scala.io.Source

object EmbeddedIndexApp extends App {

  val mappingJson = Source.fromFile("src/main/resources/uk-mapping.json").getLines().mkString

  val es = EmbeddedNode()

  es.start();
  es.createAndWaitForIndex("houseprice", Some(mappingJson))

}