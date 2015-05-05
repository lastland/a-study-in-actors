package com.liyaos

import akka.actor._

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
class BenchMarkTest {

  val nMapActors = 8
  val nLists = 10000

  @Benchmark
  def test() {
    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props(
      new ReaperActor(nMapActors + 1)), name = "reaper")
    val reduceActor = system.actorOf(Props(
      new ReduceActor((x: Int, y: Int) => x + y, 0, reaperActor)), name = "reduce")
    val mapActors = for (i <- 0 until nMapActors) yield
      system.actorOf(Props(
        new MapActor((x: Int) => x * 2, reduceActor, reaperActor)), name = "map" + i)
    for (i <- 0 until nLists) {
      for (m <- mapActors) {
        m ! List(1, 2, 3)
      }
    }
//    reduceActor ! Print
    reduceActor ! Stop
    for (m <- mapActors) {
      m ! Stop
    }
  }
}
