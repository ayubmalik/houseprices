package houseprices.csv

object DataDownloadMessages {
  case class Download(url: String, fileName: String)
  case class DownloadResult(url: String, filePath: String)
  case class DownloadFailure(msg: String)
}
