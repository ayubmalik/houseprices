package houseprices.admin.api

object DataDownloadMessages {
  case class Download(url: String, fileName: String)
  case class DownloadResult(url: String, filePath: String)
  case class DownloadFailure(msg: String)
  case class ShowActive()
  case class ActiveDownloads(count: Int)
}
