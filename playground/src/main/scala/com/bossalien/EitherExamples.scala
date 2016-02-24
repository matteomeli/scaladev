package com.bossalien

/**
 * Created by matteom on 01/10/15.
 */
object EitherExamples {
  import scala.io.Source
  import java.net.URL
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("google"))
      Left("Requested URL is blocked for the good of the people!")
    else
      Right(Source.fromURL(url))

  getContent(new URL("http://google.com")) match {
    case Left(msg) => println(msg)
    case Right(source) => source.getLines.foreach(println)
  }

  // Right projections
  val content: Either[String, Iterator[String]] =
    getContent(new URL("http://danielwestheide.com")).right.map(_.getLines())
  // content is a Right containing the lines from the Source returned by getContent
  val moreContent: Either[String, Iterator[String]] =
    getContent(new URL("http://google.com")).right.map(_.getLines)
  // moreContent is a Left, as already returned by getContent

  // Left projections
  val contentLEft: Either[Iterator[String], Source] =
    getContent(new URL("http://danielwestheide.com")).left.map(Iterator(_))
  // content is the Right containing a Source, as already returned by getContent
  val moreContentLeft: Either[Iterator[String], Source] =
    getContent(new URL("http://google.com")).left.map(Iterator(_))
  // moreContent is a Left containing the msg returned by getContent in an Iterator

  // Map
  val part5 = new URL("http://t.co/UR1aalX4")
  val part6 = new URL("http://t.co/6wlKwTmu")
  val content = getContent(part5).right.map(a =>
    getContent(part6).right.map(b =>
      (a.getLines().size + b.getLines().size) / 2)) // Either[String, Either[String, Int]]

  // FLatmap
  val contentWithFlatMap = getContent(part5).right.flatMap(a =>
    getContent(part6).right.map(b =>
      (a.getLines().size + b.getLines().size) / 2)) // Either[String, Int]

  // For comprehension
  def averageLineCount(url1: URL, url2: URL): Either[String, Int] =
    for {
      source1 <- getContent(url1).right
      source2 <- getContent(url2).right
    } yield (source1.getLines().size + source2.getLines().size) / 2

  // This does not compile
  /*def averageLineCountWontCompile(url1: URL, url2: URL): Either[String, Int] =
    for {
      source1 <- getContent(url1).right
      source2 <- getContent(url2).right
      lines1 = source1.getLines().size
      lines2 = source2.getLines().size
    } yield (lines1 + lines2) / 2*/
   // It translates to:
  def averageLineCountDesugaredWontCompile(url1: URL, url2: URL): Either[String, Int] =
    getContent(url1).right.flatMap { source1 =>
      getContent(url2).right.map { source2 =>
        val lines1 = source1.getLines().size
        val lines2 = source2.getLines().size
        (lines1, lines2)
      }.map { case (x, y) => x + y / 2 }

    }
  // The problem is that by including a value definition in our for comprehension,
  // a new call to map is introduced automatically – on the result of the previous call to map,
  // which has returned an Either, not a RightProjection.
  // As you know, Either doesn’t define a map method, making the compiler a little bit grumpy.


  // This works but it's ugly!
  def averageLineCountWorking(url1: URL, url2: URL): Either[String, Int] =
    for {
      source1 <- getContent(url1).right
      source2 <- getContent(url2).right
      lines1 <- Right(source1.getLines().size).right
      lines2 <- Right(source2.getLines().size).right
    } yield (lines1 + lines2) / 2

  // Folding. Do somethign with an Either wheter is Left or Right
  val contentWithFold: Iterator[String] =
    getContent(new URL("http://danielwestheide.com")).fold(Iterator(_), _.getLines())
  val moreContentWithFold: Iterator[String] =
    getContent(new URL("http://google.com")).fold(Iterator(_), _.getLines())

  // When to use Either?
  // 1) Error Handling
  // You can use Either for exception handling very much like Try.
  // Either has one advantage over Try: you can have more specific error types at compile time,
  // while Try uses Throwable all the time. This means that Either can be a good choice for *expected* errors.

  // Use Scalaz Disjunction. Like Either but right biased and without the these design flaws
}
