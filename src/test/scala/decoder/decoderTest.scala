package decoder


import chisel3._
import chiseltest.{ChiselScalatestTester, testableData}
import org.scalatest.freespec.AnyFreeSpec


class decoderSpec extends AnyFreeSpec with ChiselScalatestTester{
  "test decoder" in {
    test(new decoder){c =>
      c.io.x.poke(0.U)
      c.io.En.poke(false.B)
      c.io.y.expect("b0000".U)

      c.io.x.poke("b00".U)
      c.io.En.poke(true.B)
      c.io.y.expect("b0001".U)

      c.io.x.poke("b01".U)
      c.io.En.poke(true.B)
      c.io.y.expect("b0010".U)

      c.io.x.poke("b10".U)
      c.io.En.poke(true.B)
      c.io.y.expect("b0100".U)

      c.io.x.poke("b11".U)
      c.io.En.poke(true.B)
      c.io.y.expect("b1000".U)
    }
  }
}
