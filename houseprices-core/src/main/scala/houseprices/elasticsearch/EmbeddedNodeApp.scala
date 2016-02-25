package houseprices.elasticsearch

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.NodeBuilder
import houseprices.elasticsearch.config.EsClientBuilder
import scala.concurrent.Promise
import org.elasticsearch.node.Node
import scala.concurrent.Future

object EmbeddedNodeApp extends App {
  EsClientBuilder.buildClient("dev")
  println("ES up: http://localhost:9200/")
  while (true) Thread.sleep(3000)
}
