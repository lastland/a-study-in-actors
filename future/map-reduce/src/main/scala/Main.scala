package com.liyaos

object Main {
  val nLists = 80000
  def main(args: Array[String]) {
    val r = new Reduce((x: Int, y: Int) => x + y, 0)
    val m = new Map((x: Int) => x * 2, r, nLists)
    for (i <- 0 until nLists) {
      m.map(List(1, 2, 3))
    }
  }
}
