package com.bossalien

/**
 * Created by matteom on 01/10/15.
 */
object TryExamples {

  case class Customer(age: Int)
  class Cigarettes
  case class UnderAgeException(message: String) extends Exception(message)
  def buyCigarettes(customer: Customer): Cigarettes =
    if (customer.age < 16)
      throw UnderAgeException(s"Customer must be older than 16 but was ${customer.age}")
    else new Cigarettes

  val youngCustomer = Customer(15)
  try {
    buyCigarettes(youngCustomer)
    "Yo, here are your cancer sticks! Happy smokin'!"
  } catch {
    case UnderAgeException(msg) => msg
  }

  // Non fatal exceptions are caught and transformed into a Failure wrapper object
  // and returned by the apply method in the companion try object
  import scala.util.Try
  import java.net.URL
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  // getOrElse
  val url = parseURL(Console.readLine("URL: ")) getOrElse new URL("http://duckduckgo.com")

  // Support all high order collections functions like Option, chaining and mapping are possible as well
  parseURL("http://danielwestheide.com").map(_.getProtocol)
  // results in Success("http")
  parseURL("garbage").map(_.getProtocol)
  // results in Failure(java.net.MalformedURLException: no protocol: garbage)

  // map VS flatMap
  import java.io.InputStream
  def inputStreamForURL(url: String): Try[Try[Try[InputStream]]] = parseURL(url).map { u =>
    Try(u.openConnection()).map(conn => Try(conn.getInputStream))
  }

  def inputStreamForURLWithFlatMap(url: String): Try[InputStream] = parseURL(url).flatMap { u =>
    Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream))
  }

  // Filter and foreach
  def parseHttpURL(url: String) = parseURL(url).filter(_.getProtocol == "http")
  parseHttpURL("http://apache.openmirror.de") // results in a Success[URL]
  parseHttpURL("ftp://mirror.netcologne.de/apache.org") // results in a Failure[URL]
  parseHttpURL("http://danielwestheide.com").foreach(println)

  // For comprehension
  import scala.io.Source
  def getURLContent(url: String): Try[Iterator[String]] =
    for {
      url <- parseURL(url)
      connection <- Try(url.openConnection())
      is <- Try(connection.getInputStream)
      source = Source.fromInputStream(is)
    } yield source.getLines()

  // Pattern matching
  import scala.util.Success
  import scala.util.Failure
  getURLContent("http://danielwestheide.com/foobar") match {
    case Success(lines) => lines.foreach(println)
    case Failure(ex) => println(s"Problem rendering URL content: ${ex.getMessage}")
  }

  // Recover from a failure
  // If recover is called on a Success instance, that instance is returned as is.
  // Otherwise, if the partial function is defined for the given Failure instance,
  // its result is returned as a Success.
  import java.net.MalformedURLException
  import java.io.FileNotFoundException
  val content = getURLContent("garbage") recover {
    case e: FileNotFoundException => Iterator("Requested page does not exist")
    case e: MalformedURLException => Iterator("Please make sure to enter a valid URL")
    case _ => Iterator("An unexpected error has occurred. We are so sorry!")
  }
}
