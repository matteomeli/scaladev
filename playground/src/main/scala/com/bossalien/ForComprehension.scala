package com.bossalien

/**
 * Created by matteom on 06/10/15.
 */
object ForComprehension {
  val c1 = List(1, 2, 3)
  val c2 = List(4, 5, 6)
  val c3 = List("foo", "bar", "quux")

  // Example 1. "Imperative" for
  for(x <- c1; y <- c2; z <-c3) { x + y + z }
  c1.foreach(x => c2.foreach(y => c3.foreach(z => { x + y + z })))

  /*for {
    x <- c1; y <- c2; z <-c3
  } {
    val s: String = x.toString + y.toString + z
    println(s)
  }*/

  // Example 2. Yield
  for(x <- c1; y <- c2; z <- c3) yield { x + y + z }  // List[String]
  val res: List[String] = c1.flatMap(x => c2.flatMap(y => c3.map(z => { x + y + z })))

  // Example 3. Conditions
  val cond: Boolean = 2 == 2
  for(x <- c1; if cond) yield { ??? }
  // c1.withFilter(x => cond).map(x => { ??? })
  //c1.filter(x => cond).map(x => { ??? }) // If withFilter is not available

  // Example 4. Mid definitions
  for {
    x <- c1
    y = 2 } yield { ??? }
  //c1.map(x => (x, 2)).map((x, y) => { ??? })

  // For simple examples for comprehension actually look worse
  // But as soon as it get more complex map/flatMap versions get harder to understand
  // Example. What's better?
  val list = List(List(-3, -2, -1), List(0, 1, 2))

  list.flatMap(sublist => sublist.filter(elem => elem > 0).map(elem => elem.toString.length))

  for {
    subList <- list
    elem <- subList
    if elem > 0
  } yield elem.toString.length

  // Filter vs WithFilter (filter strict or non-strict depends on the object is applied to)
  /*scala> var found = false
  found: Boolean = false
  scala> List.range(1,10).filter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
  1
  3
  7
  9
  scala> found = false
  found: Boolean = false
  scala> Stream.range(1,10).filter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
  1
  3*/

  /*scala> var found = false
  found: Boolean = false
  scala> List.range(1,10).filter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
  1
  3
  7
  9
  scala> found = false
  found: Boolean = false
  scala> List.range(1,10).withFilter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
  1
  3*/
}
