object Main {

  val nLists = 40320

  def main(args: Array[String]) {
    val l = for (i <- 0 until nLists) yield List(1, 2, 3).map(
      _ * 2).foldLeft(0)((x: Int, y: Int) => x + y)
    val r = l.foldLeft(0)((x: Int, y: Int) => x + y)
    println(r)
  }
}
