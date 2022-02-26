import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class BarrelShifterSpec extends AnyFreeSpec with ChiselScalatestTester{
  "test shift" in {
    test(new BarrelShifter(32,3)){ c =>
      c.io.din.poke(1.U(32.W))
      c.io.shamt.poke(2.U)
      c.io.AL.poke(false.B)
      c.io.LR.poke(true.B)
      c.io.dout.expect(4.U)

      c.io.din.poke(4.U(32.W))
      c.io.shamt.poke(2.U)
      c.io.AL.poke(false.B)
      c.io.LR.poke(false.B)
      c.io.dout.expect(1.U)

      c.io.din.poke((1.U(32.W)))
      c.io.shamt.poke(2.U)
      c.io.AL.poke(true.B)
      c.io.LR.poke(false.B)
      c.io.dout.expect(0.U)
    }

    test(new BarrelShifter(5,3)){ c =>
      c.io.din.poke("b11111".U(10.W))
      c.io.shamt.poke(2.U)
      c.io.AL.poke(true.B)
      c.io.LR.poke(false.B)
      c.io.dout.expect("b11111".U)
    }
  }
}
