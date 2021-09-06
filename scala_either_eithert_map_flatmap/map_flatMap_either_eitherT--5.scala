import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

implicit val ec: ExecutionContext = ExecutionContext.global
val a: Future[Int] = Future(1 + 1)
a.value
a.map{ b => b + 12}
val q = a.map(d => d + 12)

val session = null

def failingSession = {
  throw new NullPointerException
}

val e = Future{
  failingSession
}

def showTheResults[T](f: Future[T]) = f onComplete {
  case Success(successStuff) => println(s"here is the success stuff: $successStuff")
  case Failure(exception) => println(s"here is the exception: $exception")
}

showTheResults[Int](a)
showTheResults[Int](e)
