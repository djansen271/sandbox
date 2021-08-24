import scala.util.{Failure, Success, Try}
// https://www.scala-exercises.org/cats/either

object EitherStyle {
  def parse(s: String): Either[NumberFormatException, Int] = {
    val numberTry = Try ( s.toInt)
    numberTry match {
      case Success(i) => Right(i)
      case Failure(exception) => Left(new NumberFormatException(s"Cannot cast $s to an Int...$exception"))
    }
  }

  def reciprocol(i: Int): Either[IllegalArgumentException, Double] = {
    val reciprocolTry = Try ( 1.0 / i )
    reciprocolTry match {
      case Success(value) => Right(value)
      case Failure(exception) => Left(new IllegalArgumentException(s"Cannot take the reciprocol of $i...$exception"))
    }
  }
  def stringify(d: Double): String = d.toString

  def magic(s: String): Either[Exception, String] = {
    val a = parse(s)
    val b = a.flatMap(reciprocol)
    val c = b.map(stringify)
    c
  }
  // magicB does the same thing as magic, just using a for comprehension
  // note that the stringify is done outside the for
  def magicB(s: String): Either[Exception, String] = {
    for {
      a <- parse(s)
      b <- reciprocol(a)
    } yield stringify(b)
  }

//  def magicC(s: String): String = {
//    for {
//      a <- parse(s)
//      b <- reciprocol(a)
//      // type mismatch
//      // found  :String
//      // required: scala.util.Either[?,?]
////      c <- stringify(b)
//      // type mismatch
//      // found: scala.util.Either[IllegalArgumentException, String]
//      // required: String
//      // a <- parse(s)
////      c = stringify(b)
//    } yield c
//  }
}

val one = EitherStyle.magic("1")
val two = EitherStyle.magic("2")
val zero = EitherStyle.magic("0")
val blah = EitherStyle.magic("blah")

val oneB = EitherStyle.magicB("1")
val two = EitherStyle.magicB("2")
val zeroB = EitherStyle.magicB("0")
val blahB = EitherStyle.magicB("blah")

def doMaps(x: Either[Exception, String]) =
  x.map(_ + " a").map(_ + " b").map(_ + " c")

doMaps(one)
doMaps(two)
doMaps(zero)
doMaps(blah)

// Starting here: With the composite function that we actually care about, we can pass in strings and then pattern match on the exception. Because Either is a sealed type (often referred to as an algebraic data type, or ADT), the compiler will complain if we do not check both the Left and Right case.

val resultOfTwo = EitherStyle.magic("2") match {
  case Left(_: NumberFormatException) => "got a NumberFormatException"
  case Left(_: IllegalArgumentException) => "got an IllegalArgumentException"
    // note that we need this generic Left case below
    // otherwise, compiler complains that cases are not exhaustive
      // match may not be exhaustive
      // It would fail on the following input: Left((x: Exception forSome x not in (IllegalArgumentException, NumberFormatException)))
      // val resultOfTwo = EitherStyle.magic("2") match {
  case Left(_) => "got some other error"
  case Right(value) => value
}

// We are here: Instead of using exceptions as our error value, let's instead enumerate explicitly the things that can go wrong in our program.
// Starting next worksheet

