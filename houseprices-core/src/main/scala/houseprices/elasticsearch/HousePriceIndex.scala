package houseprices.elasticsearch

import houseprices.postcodes.ClasspathSource
import org.elasticsearch.client.Client

object HousePriceIndex {
  val mappingJsonSource = ClasspathSource("pricepaid-uk-es-mapping.json").getLines.mkString
  def apply(client: Client) = new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource))
}