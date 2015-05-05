package com.liyaos

import scala.concurrent._
import ExecutionContext.Implicits.global

class Reduce(f: (Int, Int) => Int, init: Int) {
  var res = init
  var count = 0

  def reduce(l: List[Int]) = this.synchronized {
    res = l.foldLeft(res)(f)
    count += 1
    count
  }
}

class Map(f: Int => Int, r: Reduce, num: Int) {
  def map(l: List[Int]) = {
    val fu = future {
      l.map(f)
    }
    fu onSuccess {
      case res =>
        val re = r.reduce(l)
    }
    fu
  }
}
