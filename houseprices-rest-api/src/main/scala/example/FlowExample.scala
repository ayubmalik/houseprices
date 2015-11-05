package example

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.RunnableGraph
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source

object FlowExample {
  implicit val sys = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int](0)(_ + _)
  val graph: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  val sum = graph.run()

  println(Await.result(sum, 1 second))

  sys.terminate()

}