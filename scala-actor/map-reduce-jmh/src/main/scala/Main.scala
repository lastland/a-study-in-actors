package com.liyaos

import scala.actors.Actor
import com.liyaos.Generators._

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
class BenchMarkTest {
  val nMapActors = 8
  val nLists = 10000

  @Benchmark
  def test(): Unit = {
    val reaperActor = new ReaperActor(nMapActors + 1)
    val reduceActor = new ReduceActor((x: Int, y: Int) => x + y, 0, nLists * nMapActors, reaperActor)
    val mapActors = for (i <- 0 until nMapActors) yield
      new MapActor((x: Int) => x * 2, nLists, reduceActor, reaperActor)
    reduceActor.start
    for (m <- mapActors) {
      m.start
    }
    for (i <- 0 until nLists) {
      for (m <- mapActors) {
        m ! lists.generate
      }
    }
    reaperActor.act
  }
}
