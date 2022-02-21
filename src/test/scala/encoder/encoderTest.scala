package encoder

import chisel3._
import chiseltest.{ChiselScalatestTester, testableData}
import org.scalatest.freespec.AnyFreeSpec

class encoderTest extends AnyFreeSpec with ChiselScalatestTester{
  "test encoder" in {
    test(new encoder){ c =>
      c.io.En.poke(true.B)
      c.io.x.poke("b0000".U)
      c.io.y.expect("b00".U)
    }
  }
}
