import Generators._

import akka.actor._

object Main {

  val nJobs = 40320

  def main(args: Array[String]) {
    val nMap = args(0).toInt
    val nReduce = args(1).toInt
    val listLength = args(2).toInt
    val testList = (0 until listLength).map(_ => 1).toList

    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props[ReaperActor], name = "reaper")
    val masterActor = system.actorOf(Props(new MasterActor(
      (x: Int) => x * 2, (x: Int, y: Int) => x + y,
      nMap, nReduce, nJobs, reaperActor)))
    masterActor ! Calculate(testList)
  }
}
