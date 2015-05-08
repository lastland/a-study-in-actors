import Generators._

import akka.actor._

object Main {

  val nWorkers = 2
  val nJobs = 40320

  def main(args: Array[String]) {
    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props[ReaperActor], name = "reaper")
    val masterActor = system.actorOf(Props(new MasterActor(
      (x: Int) => x * 2, (x: Int, y: Int) => x + y,
      nWorkers, nJobs, reaperActor)))
    masterActor ! Calculate(List(1, 2, 3))
  }
}
