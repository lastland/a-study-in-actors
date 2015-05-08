package com.liyaos

import akka.actor._
import org.openjdk.jmh.annotations._

@State(Scope.Thread)
class AkkaMapReduceRemodeledTest {

  val nWorkers = 8
  val nJobs = 40320

  @Benchmark
  def test() {
    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props[ReaperActor], name = "reaper")
    val masterActor = system.actorOf(Props(new MasterActor(
      (x: Int) => x * 2, (x: Int, y: Int) => x + y,
      nWorkers, nJobs, reaperActor)))
    masterActor ! Calculate(List(1, 2, 3))
  }
}
