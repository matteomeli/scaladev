package test

import forcomp.Anagrams

object Test {

  def main(args: Array[String]) {
    val sentence = List("I", "love", "you")
    println(Anagrams.sentenceAnagrams(sentence))
  }
}
