package com.bossalien

/**
 * Created by matteom on 05/10/15.
 */
object PimpMyLibrary {
  // Implmentation of every single general function you need
  // 1) Ad-hoc polymorphism
  // 2) Traits + Implicit parameters + Implicit conversions

  // T can be whatever
  def head[T](xs: List[T]): T = ???

  // With ad-hoc polymorphism there could be some restrictions
  def show[T](t: T): String = ??? // Not all Ts can be Strings

  // Typically achieved through inheritance
  trait Show[T] {}
  def show[T <: Show[T]](t: T) = ???  // T must be a extends of Show[T] that might have a method to print

  // Also doable with traits but no inheritance. Implicits!

  // Suppose we need to do a sum function over a list of Ints and we need to generalise it
  // All the steps are shown
  def sum(xs: List[Int]): Int = xs.foldLeft(0) { (a, b) => a + b }

  // def sum[M[_], T](xs: M[T])

  def sum1(xs: List[Int]): Int = xs.foldLeft(0) { _ + _ }

  object IntMonoid {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  def sum2(xs: List[Int]): Int = xs.foldLeft(IntMonoid.mzero)(IntMonoid.mappend)

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object IntMonoid1 extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  def sum3(xs: List[Int], m: Monoid[Int]): Int = xs.foldLeft(m.mzero)(m.mappend)

  def sum4[T](xs: List[T], m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)

  def sum5[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)

  implicit val intMonoid = IntMonoid

  // Companion object for Monoid[T]. Scala will look into this object for implicits. No need for implicit val anymore ;)
  object Monoid {
    implicit object IntMonoid1 extends Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b
      def mzero: Int = 0
    }

    implicit object StringMonoid extends Monoid[String] {
      def mappend(a: String, b: String): String = a + b
      def mzero: String = ""
    }
  }

  val multMonoid = new Monoid[Int] {
    def mappend(a1: Int, a2: Int): Int = a1 * a2
    def mzero: Int = 1
  }

  object FoldLeftList {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) = xs.foldLeft(b)(f)
  }

  def sum6[T](xs: List[T])(implicit m: Monoid[T]): T = FoldLeftList.foldLeft(xs, m.mzero, m.mappend)

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
  }

  object FoldLeft {
    implicit object FoldLeftList1 extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) = xs.foldLeft(b)(f)
    }
  }

  // No reference to list or ints or anything
  def sum7[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)

  sum7(List(1, 2, 3))

  // Error!
  //List(1, 2, 3).sum7

  // How do we actually pimp the List type??
  def plus[T](a: T, b: T)(implicit m: Monoid[T]): T = m.mappend(a, b)
  plus(3, 4)

  // Error!
  //3.plus(4)

  trait Identity[A] {
    val value: A
    def plus(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2)
    def |+|(a2: A)(implicit m: Monoid[A]): A = plus(a2)
  }

  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    val value = a
  }

  3.plus(4) // It works!
  3 |+| 4 // Also works!

  trait MA[M[_], A] {
    val value: M[A]

    def sum(implicit m: Monoid[A], fl: FoldLeft[M]): A = fl.foldLeft(value, m.mzero, m.mappend)
  }

  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value = ma
  }

  List(1, 2, 3).sum // Yay!
}
