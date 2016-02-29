package houseprices.search.model

case class SearchResult(count: Int, priceData: List[PricePaidData])

case class PricePaidData(price: Int, date:String)