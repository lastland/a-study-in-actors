package com.liyaos

trait Generator[+T] {
  self => // an alias for ”this”.
  def generate: T
  def map[S](f: T => S): Generator[S] = new Generator[S] {
    def generate = f(self.generate)
  }
  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate = f(self.generate).generate
  }
}

object Generators {

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  val randomRange = 100

  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt(randomRange)
  }

  val booleans = for (x <- integers) yield x > 0

  def lists: Generator[List[Int]] = for {
    isEmpty <- booleans
    list <- if (isEmpty) emptyLists else nonEmptyLists
  } yield list

  protected def emptyLists = single(Nil)

  protected def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head :: tail
}
