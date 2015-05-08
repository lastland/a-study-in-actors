package com.liyaos

import akka.actor._
import akka.routing.RoundRobinPool

case class Calculate(l: List[Int])
case class Map(l: List[Int])
case class Reduce(l: List[Int])
case class Result(n: Int)

class MasterActor(map: Int => Int, reduce: (Int, Int) => Int,
  nWorkers: Int, nJobs: Int, reaper: ActorRef) extends Actor {

  var result = 0
  var count = 0

  val mapRouter = context.actorOf(RoundRobinPool(nWorkers).props(
    Props(new MapActor(map))), "mapRouter")
  val reduceRouter = context.actorOf(RoundRobinPool(nWorkers).props(
    Props(new ReduceActor(reduce))), "reduceRouter")

  def receive() = {
    case Calculate(l) =>
      for (i <- 0 until nJobs) mapRouter ! Map(l)
    case Reduce(l) =>
      reduceRouter ! Reduce(l)
    case Result(n) =>
      result += n
      count += 1
      if (count == nJobs) {
        reaper ! Result(result)
        context.stop(self)
      }
  }
}

class MapActor(map: Int => Int) extends Actor {
  def receive() = {
    case Map(l) =>
      sender ! Reduce(l.map(map))
  }
}

class ReduceActor(reduce: (Int, Int) => Int) extends Actor {
  def receive() = {
    case Reduce(l) =>
      sender ! Result(l.foldLeft(0)(reduce))
  }
}

class ReaperActor extends Actor {
  def receive() = {
    case Result(n) =>
      //println("The result is " + n)
      context.system.shutdown()
  }
}
