package houseprices.admin.datadownload

object DataDownloadMessages {
  case class Download(url: String, fileName: String)
  case class DownloadResult(url: String, filePath: String)
  case class DownloadFailure(msg: String)
  case class ShowActive()
  case class ActiveDownloads(count: Int, downloads: List[ActiveDownload])
  case class ActiveDownload(url: String, fileName: String, dateStarted: String)
}
