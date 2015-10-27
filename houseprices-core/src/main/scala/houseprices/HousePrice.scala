package houseprices

case class HousePrice(id: String, price: Int, date: String, address: Address)

case class Address(primaryNameNumber: String, secondaryNameNumber: String, street: String, locality: String, townCity: String, district: String, county: String)


 