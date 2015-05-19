import Generators._

import akka.actor._
import scala.util.Random

object Main {

  val nJobs = 10

  def main(args: Array[String]) {
    val nMap = args(0).toInt
    val nReduce = args(1).toInt
    val testList = List(100000000)

    val calcPi = { (n: Int) =>
      val r = new Random
      var count = 0
      for (i <- 0 until n) {
        val x = r.nextFloat
        val y = r.nextFloat
        if (x * x + y * y < 1)
          count += 1
      }
      count
    }

    val sumPi = { (x: Int, y: Int) => x + y }

    val system = ActorSystem("MapReduceSystem")
    val reaperActor = system.actorOf(Props[ReaperActor], name = "reaper")
    val masterActor = system.actorOf(Props(new MasterActor(
      calcPi, sumPi,
      nMap, nReduce, nJobs, reaperActor)))
    masterActor ! Calculate(testList)
  }
}
