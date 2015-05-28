import akka.actor._
import scala.io.Source

object Main {

  def main(args: Array[String]) {
    val iter = args(0).toInt

    val lines = Source.fromFile("testdata").getLines
    val n = lines.next.toInt
    val system = ActorSystem("PageRankSystem")
    val master = system.actorOf(Props(new MasterActor(n)), name = "master")
    for (line <- lines) {
      val a = line.split(" ").map(_.toInt)
      master ! AddLink(a(0), a(1))
    }
    master ! Calculate(iter)
  }
}
