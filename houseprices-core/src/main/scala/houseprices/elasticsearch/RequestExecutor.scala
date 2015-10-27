package houseprices.elasticsearch

import org.elasticsearch.action.ActionResponse
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.ActionRequestBuilder
import scala.concurrent.Promise
import scala.concurrent.Future

class RequestExecutor[T <: ActionResponse] extends ActionListener[T] {
  private val promise = Promise[T]()

  def onResponse(response: T) {
    promise.success(response)
  }

  def onFailure(e: Throwable) {
    promise.failure(e)
  }

  def execute[RB <: ActionRequestBuilder[_, T, _, _]](request: RB): Future[T] = {
    request.execute(this)
    promise.future
  }
}

object RequestExecutor {
  def apply[T <: ActionResponse](): RequestExecutor[T] = {
    new RequestExecutor[T]
  }
}
