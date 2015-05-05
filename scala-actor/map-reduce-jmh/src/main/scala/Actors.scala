package com.liyaos

import scala.actors.Actor
import scala.actors.Actor._

case object Stop
case object Print

class ReduceActor(f: (Int, Int) => Int, init: Int, total: Int, reaper: Actor) extends Actor {
  var res = init
  var count = 0

  def act() {
    while (true) {
      receive {
        case l: List[Int] =>
          res = l.foldLeft(res)(f)
          count += 1
          if (count >= total) {
            reaper ! Stop
            exit()
          }
      }
    }
  }
}

class MapActor(f: Int => Int, total: Int, r: Actor, reaper: Actor) extends Actor {
  var count = 0

  def act() {
    while (true) {
      receive {
        case l: List[Int] =>
          r ! (l.map(f))
          count += 1
          if (count >= total) {
            reaper ! Stop
            exit()
          }
      }
    }
  }
}

class ReaperActor(souls: Int) extends Actor {
  var collected = 0
  def act() {
    while (true) {
      receive {
        case Stop =>
          collected += 1
          if (souls == collected) {
            exit()
          }
      }
    }
  }
}
