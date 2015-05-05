import util.Random
import Generators._

object Main {
  val nMapActors = 8
  val nLists = 1000000

  def main(args: Array[String]) {
    val reduceActor = new ReduceActor((x: Int, y: Int) => x + y, 0)
    val mapActors = for (i <- 0 until nMapActors) yield
      new MapActor((x: Int) => x * 2, reduceActor)
    reduceActor.start
    for (m <- mapActors) {
      m.start
    }
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
