import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class bcd7segSpec extends AnyFlatSpec with ChiselScalatestTester{
  it should "test bcd7seg en is false" in {
    test(new bcd7seg){ c =>
      c.io.num.poke(3.U)
      c.io.en.poke(false.B)
      c.io.HEX.expect("b1111111".U)
    }
  }

  it should "test bcd7seg" in {
    test(new bcd7seg){ c =>
      c.io.num.poke(3.U)
      c.io.en.poke(true.B)
      c.io.HEX.expect("b0110000".U)
    }
  }
}
