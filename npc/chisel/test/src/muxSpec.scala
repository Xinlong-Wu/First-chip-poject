import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class Mux24 extends AnyFlatSpec with ChiselScalatestTester {
//  behavior of "MyModule"
  it should "test Mux24" in {
    test(new mux24){ c =>
      c.io.y.poke("b00".U)
      c.io.x0.poke("b00".U)
      c.io.x1.poke("b01".U)
      c.io.x2.poke("b10".U)
      c.io.x3.poke("b11".U)
      c.io.f.expect("b00".U)
    }
  }
}