package model

import model.Mark.Mark

object Mark extends Enumeration {
  type Mark = Value
  val Empty = Value("_")
  val X = Value("X")
  val O = Value("O")
}

class Board(size: Int) {
  require(size > 1)

  private val BoardLength = size * size
  private val board: Array[Mark] = Array.fill(BoardLength)(Mark.Empty)
  private var expected = Mark.X

  def expectedMark = expected

  def isFull = !board.contains(Mark.Empty)

  // true - ok step, false - bad step
  def step(index: Int, mark: Mark) = {
    if (index >= 0 && index < BoardLength && board(index) == Mark.Empty && mark == expected) {
      board(index) = mark
      toggleExpected()
      true
    } else false
  }

  def isWin: Mark = Board.isWin(board, size)

  def toBoardString = Board.toBoardString(board, size)

  def toFreeString = Board.toFreeString(board, size)

  private def toggleExpected(): Unit = {
    expected = if (expected == Mark.X) Mark.O else Mark.X
  }
}

object Board {
  // split to possible vectors and check for solution
  def isWin[A <: Mark](a: Array[A], n: Int): Mark = {
    val r = (hsplit(a, n) ++ vsplit(a, n) ++ xsplit(a, n))
      .find(x => x.count(_ == Mark.X) == n || x.count(_ == Mark.O) == n)
    r match {
      case None => Mark.Empty
      case Some(Seq(x, _*)) => x
    }
  }

  // board.grouped(size).map(_.toList).toArray
  def hsplit[A](a: Array[A], n: Int) = a.grouped(n).map(_.toVector).toSeq

  def vsplit[A](a: Array[A], n: Int) = {
    for (x <- 0 until n) yield {
      for (y <- 0 until n) yield a(x + y * n)
    }.toSeq
  }

  def xsplit[A](a: Array[A], n: Int) = {
    val x1 = for (x <- 0 until n) yield a(x * n + x)
    val x2 = for (x <- 0 until n) yield a(x * n + n - x - 1)
    Seq(x1, x2)
  }

  // show board
  def toBoardString[A](a: Array[A], n: Int, cr: String = "\n") = {
    a.grouped(n).map(_.mkString(" ")).mkString(cr)
  }

  // show free movies
  def toFreeString[A](a: Array[A], n: Int, cr: String = "\n") = {
    a.zipWithIndex.grouped(n).map(_.map(x => if (x._1 == Mark.Empty) x._2 else ".").mkString(" ")).mkString(cr)
  }

  def apply(n: Int) = new Board(n)
}
