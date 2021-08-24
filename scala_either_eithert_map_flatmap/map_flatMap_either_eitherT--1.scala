// Question:
// I feel like I've heard "A thing has a wrapper on it. Use flatMap to get into the thing."
// I think that both .map and .flatMap accomplish the same thing for that part of it.
// They unwrap one level of the object.
// It's just that .flatMap adds the piece of doing a map, but then calling .flatten at the end.
// The mistake I came into this with:

val p1 = Vector(Some(3), Some(4), None)
//  I thought that
//    p1.flatMap(_ + 12)
//  would unwrap the options to get at the Ints
// Instead, this gives a type mismatch

// Calling
p1.map(x => x.getOrElse(0) + 12)
// yields something more like I was looking for
// Vector(15, 16, 12)

// We could also do
p1.flatten
// which gives Vector(3,4)

// So now where am I at? I think a thing I hear a lot is "we want to get into the SSResponse.
// In order to understand that, I'm going to need to understand EitherT
// I'm gonna start with Either

import cats.data.EitherT

import scala.util.Try
import cats.implicits._

val bag = List("1", "2", "three", "4", "one hundred seventy five")

def listOfStringsToInts(l: List[String]): List[Int] = {
  l.map(x => Try(x.toInt).toOption).flatten
}

listOfStringsToInts(bag)

def listOfStringsToInts2(l: List[String]): List[Int] = {
  l.flatMap(x => Try(x.toInt).toOption)
}

listOfStringsToInts2(bag)

// Below is a vector of options. Current hypothesis: the .map
// or .flatmap gets us into ONE level of the structure (the vector)
// but the thing we're dealing with is still an option
val v1 = Vector(Some(1), Some(2), None)
val v2 = v1.map {x =>
  x match {
    case Some(a) => a + 4
    case None => 0
  }
}

val v3: Option[Int] = Some(33)
v3.map(_ + 100)  // Some(133)
// v3.map(_ + 100).flatten  // Error:(31, 17) Cannot prove that Int <:< Option[B].
val v4: Option[Int] = None
v4.map(_ + 100) // None
// v4.map(_ + 100).flatten // Error:(34, 17) Cannot prove that Int <:< Option[B].
//v4.map(_ + 100).get // java.util.NoSuchElementException: None.get
v4.map(_ + 100).getOrElse(0)
v3.map(_ + 100).get // 133 ... because it is a Some()
v3.map(_ + 100).getOrElse(0) // 133


// ******* Either https://www.scala-lang.org/api/current/scala/util/Either.html
val v5: Either[Int, String] = Left(99)
// val v6: Either[Int, String] = Left("stuff") // Error:(41, 36) type mismatch;
  //found   : String("stuff")
  //required: Int
def log[A, B](v: Either[A, B]): Unit = {
    v match {
      case Left(x) => println(s"Error: $x received")
      case Right(y) => println(s"Everything's cool. Received '$y'")
    }
  }
log(v5)
val v7: Either[Int, String] = Right("things went well")
v7.left // LeftProjection(Right(things went well))
log(v7)

// ******* EitherT
// Ah, EitherT is a cats thing https://typelevel.org/cats/datatypes/eithert.html
// whereas Either is a Scala Standard Library thing

val v8: EitherT[List, String, Int] = EitherT[List, String, Int](List(Left("10"), Right(20)))
val v9 = v8.fold(s => s.toInt, i => i)

val v10: EitherT[List, String, Int] = EitherT[List, String, Int](List(Left("potatoes"), Right(50)))
// Below, the Right side receives the map, but the Left side is left alone
val v11 = v10.map(x => x + 100) // EitherT(List(Left(potatoes), Right(150)))
// val v12 = v10.map(x => x + 100).flatten  // Error:(62, 33) Cannot prove that Int <:< cats.data.EitherT[List,String,B].
