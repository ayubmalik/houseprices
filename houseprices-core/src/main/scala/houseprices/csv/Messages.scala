package houseprices.csv

object Messages {
  case class Download(url: String, fileName: String)
  case class DownloadResult(url: String, filePath: String)
  case class Failure(msg: String)
}
