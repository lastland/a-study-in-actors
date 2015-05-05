import scala.actors.Actor
import scala.actors.Actor._

case object Stop
case object Print

class ReduceActor(f: (Int, Int) => Int, init: Int) extends Actor {
  var res = init
  def act() {
    while (true) {
      receive {
        case l: List[Int] =>
          res = l.foldLeft(res)(f)
        case Print =>
          println(res)
        case Stop =>
          exit()
      }
    }
  }
}

class MapActor(f: Int => Int, r: ReduceActor) extends Actor {
  def act() {
    while (true) {
      receive {
        case l: List[Int] =>
          r ! (l.map(f))
        case Stop =>
          exit()
      }
    }
  }
}
