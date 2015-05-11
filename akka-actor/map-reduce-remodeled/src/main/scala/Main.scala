import Generators._

import akka.actor._

object Main {

  val nJobs = 40320

  def main(args: Array[String]) {
    val nWorkers = args(0).toInt
    val listLength = args(1).toInt
    val testList = (0 until listLength).map(_ => 1).toList

    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props[ReaperActor], name = "reaper")
    val masterActor = system.actorOf(Props(new MasterActor(
      (x: Int) => x * 2, (x: Int, y: Int) => x + y,
      nWorkers, nJobs, reaperActor)))
    masterActor ! Calculate(testList)
  }
}
