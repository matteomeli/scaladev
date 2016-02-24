package com.bossalien

/**
 * Created by matteom on 01/10/15.
 */
case class User(
                 id: Int,
                 firstName: String,
                 lastName: String,
                 age: Int,
                 gender: Option[String])

object UserRepository {
  private val users = Map(1 -> User(1, "John", "Doe", 32, Some("male")),
    2 -> User(2, "Johanna", "Doe", 30, None))

  def findById(id: Int): Option[User] = users.get(id)

  def findAll = users.values
}

object TestUser {

  val user1 = UserRepository.findById(1)
  if (user1.isDefined) {
    println(user1.get.firstName)
  }
  // will print "John"

  val user4 = UserRepository.findById(4)
  println(user1.get.firstName)
  // will generate an exception because user4 is None

  // GetOrElse
  val user5 = User(2, "Johanna", "Doe", 30, None)
  println("Gender: " + user5.gender.getOrElse("not specified"))
  // will print "not specified"

  // Pasttern matching
  val user6 = User(2, "Johanna", "Doe", 30, None)
  user6.gender match {
    case Some(gender) => println("Gender: " + gender)
    case None => println("Gender: not specified")
  }

  val user7 = User(2, "Johanna", "Doe", 30, None)
  val gender0 = user7.gender match {
    case Some(g) => g
    case None => "not specified"
  }
  println("Gender: " + gender0)

  // Option as collection
  UserRepository.findById(2).foreach(user => println(user.firstName))
  // prints "Johanna"

  val age = UserRepository.findById(1).map(_.age) // age is Some(32)

  val gender = UserRepository.findById(1).map(_.gender) // gender is an Option[Option[String]]

  val gender1 = UserRepository.findById(1).flatMap(_.gender)
  // gender is Some("male")
  val gender2 = UserRepository.findById(2).flatMap(_.gender)
  // gender is None
  val gender3 = UserRepository.findById(3).flatMap(_.gender) // gender is None

  // Map VS flatMpa
  val names = List(List("John", "Johanna", "Daniel"), List(), List("Doe", "Westheide"))
  names.map(_.map(_.toUpperCase))
  // results in List(List("JOHN", "JOHANNA", "DANIEL"), List(), List("DOE", "WESTHEIDE"))
  names.flatMap(_.map(_.toUpperCase))
  // results in List("JOHN", "JOHANNA", "DANIEL", "DOE", "WESTHEIDE")

  // map VS flatMap and Options
  val namesOption = List(Some("Johanna"), None, Some("Daniel"))
  namesOption.map(_.map(_.toUpperCase)) // List(Some("JOHANNA"), None, Some("DANIEL"))
  namesOption.flatMap(xs => xs.map(_.toUpperCase)) // List("JOHANNA", "DANIEL")

  // For comprehension
  for {
    user <- UserRepository.findById(1)
    gender <- user.gender
  } yield gender // results in Some("male")

  for {
    user <- UserRepository.findAll
    gender <- user.gender
  } yield gender

  for {
    User(_, _, _, _, Some(gender)) <- UserRepository.findAll
  } yield gender

  // Chaining
  case class Resource(content: String)

  val resourceFromConfigDir: Option[Resource] = None
  val resourceFromClasspath: Option[Resource] = Some(Resource("I was found on the classpath"))
  val resource = resourceFromConfigDir orElse resourceFromClasspath
}
