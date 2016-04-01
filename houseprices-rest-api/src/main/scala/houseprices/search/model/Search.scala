package houseprices.search.model

import java.time.LocalDate

case class Query(text: String)

case class SearchResult(count: Int, priceData: List[PricePaidData])

case class PricePaidData(price: Int, dateSold: String, address: String)