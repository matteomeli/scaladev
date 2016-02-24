package com.bossalien

import java.util.NoSuchElementException

trait MyList[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: MyList[T]
}

class MyCons[T](val head: T, val tail: MyList[T]) extends MyList[T] {
  def isEmpty = false
}

object MyNil extends MyList[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

//val l: List[String] = MyNil
// Make list covariant!
//def prepend(elem: T): List[T] = new Cons(elem, this) ???
//def prepend [U >: T] (elem: U): List[U] = new Cons(elem, this)
