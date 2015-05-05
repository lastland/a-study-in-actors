import Generators._

import akka.actor._

object Main {

  val nMapActors = 8
  val nLists = 1000000

  def main(args: Array[String]) {
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
        m ! lists.generate
      }
    }
    reduceActor ! Print
    reduceActor ! Stop
    for (m <- mapActors) {
      m ! Stop
    }
  }
}
