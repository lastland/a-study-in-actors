import akka.actor._

case class Calculate(iter: Int)
case object Spread
case class Transfer(mass: Double)
case object Finish
case class AddLink(from: Int, to: Int)
case class AddNeighbour(from: ActorRef, to: ActorRef)
case object WillSendToYou

object Constants {
  val d = 0.85
}

class MasterActor(n: Int) extends Actor with ActorLogging {

  val actors =
    for (i <- 0 until n)
    yield context.actorOf(Props(new NodeActor(1.0, n)), name = "node"+i)

  def receive() = normal

  def normal: Receive = {
    case Calculate(iter: Int) =>
      for (actor <- actors) actor ! Spread
      context.become(calc(iter, 0))
    case AddLink(a, b) =>
      actors(a) ! AddNeighbour(actors(a), actors(b))
  }

  def calc(iter: Int, count: Int): Receive = {
    case Finish =>
      log.debug(s"finish received from $sender")
      if (count + 1 == n) {
        if (iter - 1 == 0) {
          log.debug("all jobs done, system shutting down")
          context.system.shutdown
        } else {
          log.debug(s"iteration $iter finished")
          for (actor <- actors) actor ! Spread
          context.become(calc(iter - 1, 0))
        }
      } else {
        log.debug(s"iteration $iter $count jobs finished")
        context.become(calc(iter, count + 1))
      }
    case AddNeighbour(_, _) =>
      println("Adding A Neighbour during calculating is prohibited.")
  }
}

class NodeActor(initialMass: Double, n: Int) extends Actor with ActorLogging {

  import Constants._

  var mass = initialMass
  var neighbours: List[ActorRef] = List()
  var acquiredMass = 0.0
  var acquiredNeighbours = 0
  var expected = 0

  var reportTo: Option[ActorRef] = None

  def receive = normal

  def normal: Receive = {
    case AddNeighbour(a, b) if a == self =>
      neighbours = b :: neighbours
      b ! WillSendToYou
    case WillSendToYou =>
      expected += 1
    case Spread =>
      reportTo = Some(sender)
      if (neighbours.size > 0) {
        val m = mass / neighbours.size
        mass = 1 - d
        log.info(s"$self transfer $m to each, $mass left")
        for (neighbour <- neighbours)
          neighbour ! Transfer(m)
      }
      if (expected == 0) {
        sender ! Finish
      }
    case Transfer(m: Double) =>
      acquiredMass += m
      acquiredNeighbours += 1
      if (acquiredNeighbours == expected) {
        mass = 1 - d + d * acquiredMass
        acquiredMass = 0.0
        acquiredNeighbours = 0
        log.info(s"new mass of $self: $mass")
        reportTo match {
          case Some(r) => r ! Finish
          case None => ()
        }
      }
  }
}
