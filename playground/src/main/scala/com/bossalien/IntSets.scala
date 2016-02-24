package com.bossalien

/**
 * Created by matteom on 05/10/15.
 */
object IntSets {
  abstract class IntSet {
    def include(x: Int): IntSet
    def contains(x: Int): Boolean
    def union(other: IntSet): IntSet
  }

  class NonEmpty(element: Int, left: IntSet, right: IntSet) extends IntSet {
    override def toString = "{" + left + element + right + "}"

    def contains(x: Int): Boolean =
      if (x < element) left contains x
      else if (x > element) right contains x
      else true

    def include(x: Int): IntSet =
      if (x < element) new NonEmpty(element, left include x, right)
      else if (x > element) new NonEmpty(element, left, right include x)
      else this

    def union(other: IntSet): IntSet = ((left union right) union other) include element
  }

  object Empty extends IntSet {
    override def toString = "."
    def contains(x: Int): Boolean = false
    def include(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
    def union(other: IntSet): IntSet = other
  }

  val a: Array[NonEmpty] = Array(new NonEmpty(1, Empty, Empty))
  val b: Array[IntSet] = a
  b(0) = IntSets.Empty
  val s: NonEmpty = a(0)

  //class C[+A] { ??? } // C is covariant
  //class C[-A] { ??? } // C is contravariant
  //class C[A] { ??? }  // C is nonvariant
}
