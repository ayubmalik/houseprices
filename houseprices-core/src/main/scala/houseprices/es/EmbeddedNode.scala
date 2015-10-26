package houseprices.es

import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.io.IOException
import java.nio.file.Paths
import java.nio.file.FileVisitResult
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder
import java.nio.file.Path
import org.elasticsearch.cluster.metadata.MetaDataDeleteIndexService.Request
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest

class EmbeddedNode(settings: Map[String, String]) {

  val settingsWithDefaults = (settings.keySet ++ EmbeddedNode.defaultSettings.keySet).map { key =>
    key -> settings.getOrElse(key, EmbeddedNode.defaultSettings(key))
  } toMap

  val dataPath = settingsWithDefaults.getOrElse("path.data", Files.createTempDirectory("tmpHousePrices-").toString)

  import scala.collection.JavaConversions.mapAsJavaMap
  val settingsBuilder = ImmutableSettings.builder().put(mapAsJavaMap(settingsWithDefaults))

  if (!settingsWithDefaults.contains("path.data"))
    settingsBuilder.put("path.data", dataPath)

  private lazy val node = NodeBuilder.nodeBuilder()
    .local(true)
    .settings(settingsBuilder.build())
    .build()

  def client() = {
    node.client()
  }

  def start() = {
    node.start()
    this
  }

  def createAndWaitForIndex(indexName: String, mappingJson: Option[String]): Unit = {
    client.admin.indices.prepareCreate(indexName).execute.actionGet()
    mappingJson.map { json =>
      val mappingRequest = new PutMappingRequest(indexName).`type`("uk").source(json)
      client.admin().indices.putMapping(mappingRequest).actionGet();
    }
    client.admin.cluster.prepareHealth(indexName).setWaitForActiveShards(1).execute.actionGet()
  }

  def shutdown(delete: Boolean = true) = {
    node.stop()
    node.close()
    if (delete)
      deleteData
  }

  def deleteData() = {
    try {
      Files.walkFileTree(Paths.get(dataPath), new SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          Files.deleteIfExists(file)
          super.visitFile(file, attrs)
        }
        override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
          Files.deleteIfExists(dir)
          super.postVisitDirectory(dir, exc)
        }
      })
    } catch {
      case e: IOException =>
        throw new RuntimeException("Could not delete data directory of embedded elasticsearch server", e);
    }
  }
}

object EmbeddedNode {
  val defaultSettings = Map(
    "node.name" -> "HousePrices",
    "http.enabled" -> "true")

  def apply() = {
    new EmbeddedNode(defaultSettings)
  }
}
