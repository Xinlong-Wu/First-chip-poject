package mux

import chisel3.fromIntToLiteral
import chiseltest.{ChiselScalatestTester, testableData}
import org.scalatest.freespec.AnyFreeSpec

class MUXSpec extends AnyFreeSpec with ChiselScalatestTester {
  "MUX Test" in {
    test(new MUX) { c =>
      c.io.a.poke(1.U)
      c.io.b.poke(0.U)
      c.io.s.poke(0.U)
      c.io.y.expect(1.U)
    }
  }
}