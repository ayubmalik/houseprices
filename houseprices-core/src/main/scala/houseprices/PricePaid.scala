package houseprices

case class PricePaid(id: String, price: Int, date: String, address: Address)

case class Address(postcode: String, primaryName: String, secondaryName: String, street: String, locality: String, townCity: String, district: String, county: String)


 