package com.liyaos

import org.openjdk.jmh.annotations._


@State(Scope.Thread)
class Test {
  val nLists = 80000

  @Benchmark
  def test() {
    val r = new Reduce((x: Int, y: Int) => x + y, 0)
    val m = new Map((x: Int) => x * 2, r, nLists)
    for (i <- 0 until nLists) {
      m.map(List(1, 2, 3))
    }
  }
}
