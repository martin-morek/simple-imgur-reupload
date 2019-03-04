package commons

case class UrlUploadRequest(urls: List[String])

case class ErrorResponseData(error: String, request: String, method: String)

case class ErrorResponse(data: ErrorResponseData, success: Boolean, status: Int)
