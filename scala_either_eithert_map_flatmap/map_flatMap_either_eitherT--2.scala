import scala.util.{Failure, Success, Try}
// https://www.scala-exercises.org/cats/either

val rght: Either[String, Int] = Right(12)
rght.map(_ + 1) // Right(13)
rght.map(_ + 1).getOrElse("blah") // 13
rght.map(_ + 1).isRight // true

val lft: Either[String, Int] = Left("Error occurred")
lft.map(_ + 1) // Left(Error occurred)
lft.map(_ + 1).getOrElse("blah") // blah
lft.isRight
lft.isLeft
lft.map(_ + 1).isLeft

def addOne(i: Int): Either[String, Int] = {
  println(s"First line of addOne...")
  val addOneTry = Try(i + 1)
  addOneTry match {
    case Success(value) => Right(value)
    case Failure(exception) => Left(s"Failed to add 1 to $i")
  }
}
rght.flatMap(addOne)
lft.flatMap(addOne) // Left(Error occurred)
                    // Note that this is the original Left message
                    // the println at beginning of addOne does not get hit
                    // we don't get into addOne -- it just short-circuits
                    // and returns the original Left message

rght.flatMap(x => Right(x + 1)) // Right(13)
rght.map(x => x + 1) // Right(13)
// so, the above is interesting -- the flatMap here doesn't seem super-useful
// the map gets us into the value

// below, calling flatten doesn't work (and can't import cats implicits)
//rght.map(x => x + 1).flatten // Right(13)

rght.map(x => x + 1).getOrElse("blah") // 13

lft.flatMap(y => Right(y + 1)) // same as lft
lft.map(y => y + 1) // same as lft
// below, .get is not available , gotta do .getOrElse
lft.map(y => y + 1).getOrElse("blah") // blah

