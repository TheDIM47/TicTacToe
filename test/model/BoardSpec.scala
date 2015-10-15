package model

import org.specs2.mutable._

class BoardSpec extends Specification {
  "Board" should {

    "be empty and not win" in {
      Board(2).isFull must_== false
      Board(2).isWin must_== Mark.Empty
    }

    "have custom size and be empty and not win" in {
      Board(4).isFull must_== false
      Board(5).isWin must_== Mark.Empty
    }

    "first expected mark is X" in {
      Board(2).expectedMark must_== Mark.X
      Board(2).expectedMark must_== Mark.X
    }

    "not switch expected mark on invalid move" in {
      val board = Board(3)
      board.expectedMark must_== Mark.X
      board.step(-1, Mark.X) must_== false
      board.expectedMark must_== Mark.X

      board.step(1, Mark.X) must_== true
      board.expectedMark must_== Mark.O
      board.step(1, Mark.O) must_== false
      board.expectedMark must_== Mark.O
    }

    "switch expected mark on valid move" in {
      val board = Board(3)
      board.expectedMark must_== Mark.X
      board.step(0, Mark.X) must_== true

      board.expectedMark must_== Mark.O
      board.step(1, Mark.O) must_== true

      board.expectedMark must_== Mark.X
      board.step(2, Mark.X) must_== true

      board.expectedMark must_== Mark.O
      board.step(3, Mark.O) must_== true
    }

    "win X" in {
      val board = Board(2)
      board.step(0, Mark.X) must_== true
      board.isWin must_== Mark.Empty
      board.step(1, Mark.O) must_== true
      board.isWin must_== Mark.Empty
      board.step(2, Mark.X) must_== true
      board.isWin must_== Mark.X
      board.isFull must_== false
    }

    "win O" in {
      val board = Board(3)
      board.step(1, Mark.X) must_== true
      board.isWin must_== Mark.Empty
      board.step(2, Mark.O) must_== true
      board.isWin must_== Mark.Empty
      board.step(3, Mark.X) must_== true
      board.isWin must_== Mark.Empty
      board.step(5, Mark.O) must_== true
      board.isWin must_== Mark.Empty
      board.step(7, Mark.X) must_== true
      board.isWin must_== Mark.Empty
      board.step(8, Mark.O) must_== true
      board.isWin must_== Mark.O
      board.isFull must_== false
    }

    "be full and not win" in {
      val board = Board(3)
      board.step(0, Mark.X) must_== true
      board.step(1, Mark.O) must_== true
      board.step(2, Mark.X) must_== true
      board.step(6, Mark.O) must_== true
      board.step(3, Mark.X) must_== true
      board.step(5, Mark.O) must_== true
      board.step(4, Mark.X) must_== true
      board.step(8, Mark.O) must_== true
      board.step(7, Mark.X) must_== true
      board.isWin must_== Mark.Empty
      board.isFull must_== true
    }
  }
}
