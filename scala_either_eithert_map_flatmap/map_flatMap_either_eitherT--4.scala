import scala.util.{Failure, Success, Try}

// ADT = Algebraic Data Type -> sealed type
object EitherStyleWithAdts  {
  sealed abstract class Error
  final case class NotANumber(string: String) extends Error
  final case object NoZeroReciprocal extends Error

  def parse(s: String): Either[Error, Int] = {
    val intTry = Try(s.toInt)
    intTry match {
      case Failure(exception) => Left(NotANumber(s"Cannot turn $s into a number"))
      case Success(value) => Right(value)
    }
  }

  def reciprocal(i: Int): Either[Error, Double] = {
    val recipTry = Try(1.0 / i)
    recipTry match {
      case Failure(_) => Left(NoZeroReciprocal)
      case Success(value) => Right(value)
    }
  }
  def stringify(d: Double): String = d.toString

  def magic(s: String): Either[Error, String] = {
    val a = parse(s)
    // notation on flatMap:
    // Binds the given function across Right
//    def flatMap[A1 >: A, B1](f: B => Either[A1, B1]): Either[A1, B1] = this match {
//      case Right(b) => f(b)
//      case _        => this.asInstanceOf[Either[A1, B1]]
//    }
    val b = a.flatMap(reciprocal)
    val c = b.map(stringify)
    c
  }
}

val resultOfTwo = EitherStyleWithAdts.magic("2") match {
  case Left(EitherStyleWithAdts.NotANumber(_)) => "Input was not a number"
  case Left(EitherStyleWithAdts.NoZeroReciprocal) => "Cannot take the reciprocal of zero"
    // Now, the two error cases are part of a sealed class
    // We have exhaustive pattern matching and do not need a "_" case
  case Right(result) => s"Got reciprocal: ${result}"
}

// https://www.scala-exercises.org/cats/either
// Left off:  Either in the small, Either in the large

// Maybe this should be next: https://typelevel.org/cats/datatypes/eithert.html
