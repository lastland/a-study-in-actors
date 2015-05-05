package com.liyaos

import akka.actor._

case object Stop
case object Print

class ReduceActor(f: (Int, Int) => Int, init: Int, reaper: ActorRef) extends Actor {
  var res = init

  def receive() = {
    case l: List[Int] =>
      res = l.foldLeft(res)(f)
    case Print =>
      println(res)
    case Stop =>
      reaper ! Stop
      context.stop(self)
  }
}

class MapActor(f: Int => Int, r: ActorRef, reaper: ActorRef) extends Actor {
  def receive() = {
    case l: List[Int] =>
      r ! (l.map(f))
    case Stop =>
      reaper ! Stop
      context.stop(self)
  }
}

class ReaperActor(souls: Int) extends Actor {
  var collected = 0
  def receive() = {
    case Stop =>
      collected += 1
      if (souls == collected)
        context.system.shutdown()
  }
}
