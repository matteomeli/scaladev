
object lesson1 {
  println("Welcome to Scala!")
  def factorial(n: Int): Int = {
    if (n == 0) 1
    else n * factorial(n - 1)
  }
  def sqrtIter(guess: Double, x: Double):
  Double =
    if (isGoodEnough(guess, x)) guess
    else sqrtIter(improve(guess, x), x)
  def improve(guess: Double, x: Double) = (guess + x / guess) / 2
  def isGoodEnough(guess: Double, x: Double) = abs(guess * guess - x) < 0.001
  def abs(x: Double) = if (x < 0) -x else x
  def sqrt(x: Double) = sqrtIter(1.0, x)
  class Rational(x: Int, y: Int) {
    require(y != 0, "denominator must be non-zero")

    def this(x: Int) = this(x, 1)

    private def gcd(a: Int, b: Int): Int =
      if (b == 0) a else gcd(b, a % b)

    def numer = x

    def denom = y

    def +(that: Rational) = {
      new Rational(
        numer * that.denom + that.numer * denom,
        denom * that.denom)
    }

    def +(i: Int): Rational =
      new Rational(numer + i * denom, denom)

    def unary_- : Rational = new Rational(-numer, denom)

    def -(that: Rational) = this + -that

    def -(i: Int): Rational =
      new Rational(numer - i * denom, denom)


    def *(that: Rational): Rational =
      new Rational(numer * that.numer, denom * that.denom)


    def *(i: Int): Rational =
      new Rational(numer * i, denom)

    def /(that: Rational): Rational =
      new Rational(numer * that.denom, denom * that.numer)

    def /(i: Int): Rational =
      new Rational(numer, denom * i)

    def <(that: Rational) = numer * that.denom < that.numer * denom

    def max(that: Rational) = if (this < that) that else this

    override def toString = {
      val g = gcd(numer, denom)
      numer / g + "/" + denom / g // Could this be a problem? This approach would be problematic for big numbers, better to simplify early
    }
  }

  val x = new Rational(1, 2)
  x.numer
  x.denom
  val y = new Rational(2, 3)
  x + y
  val a = new Rational(1, 3)
  val b = new Rational(5, 7)
  val c = new Rational(3, 2)
  a - b + c
  b + b
  a < b
  a max b
  new Rational(2)
  implicit def intToRational(x: Int): Rational = new Rational(x)
  2 + a
  // Lists implementation exercises
  def last[T](xs: List[T]): T = xs match {
    case List() => throw new Error("last of empty list")
    case List(x) => x
    case y :: ys => last(ys)
  }

  def init[T](xs: List[T]): List[T] = xs match {
    case List() => throw new Error("init of empty list")
    case List(x) => List()
    case y :: ys => y :: init(ys)
  }

  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List() => ys
    case z :: zs => z :: concat(zs, ys)
  }


  // Slow but we’ll come back to it
  def reverse[T](xs: List[T]): List[T] = xs match {
    case List() => xs
    case y :: ys => reverse(ys) ++ List(y)
  }

  def removeAt(n: Int, xs: List[Int]) = (xs take n) ::: (xs drop n + 1)
  def flatten(xs: List[Any]): List[Any] = xs match {
    case Nil => Nil
    case head :: Nil => List(head)
    case head :: tail => (head match {
      case l: List[Any] => flatten(l)
      case i => List(i)
    }) ::: flatten(tail)
  }

  def msort[T](list: List[T])(implicit ord: math.Ordering[T]): List[T] = {
    val mid = list.length / 2
    if (mid == 0) list
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }
      val (first, second) = list.splitAt(mid)
      merge(msort(first), msort(second))
    }
  }
  println(msort(List(2, 1, 5, 2, 76, 7, 2)))
}
