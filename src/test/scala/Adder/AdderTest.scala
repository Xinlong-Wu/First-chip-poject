package Adder

import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class AdderTest extends AnyFreeSpec with ChiselScalatestTester{

  "test FullAdder" in {
    test(new FullAdder){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(1.U)
      c.io.overflow.expect(0.U)

      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(0.U)
      c.io.overflow.expect(1.U)

      c.io.in1.poke(0.U)
      c.io.in2.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(0.U)
      c.io.overflow.expect(0.U)
    }
  }

  "test Adder" in {
    test(new Adder(4)){c =>
      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1100".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b1111".U)
      c.io.overflow.expect(0.U)

      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1110".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b0001".U)
      c.io.overflow.expect(1.U)
    }
  }
}
