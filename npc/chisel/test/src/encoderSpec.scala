
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class encoderSpec extends AnyFlatSpec with ChiselScalatestTester {
  //  behavior of "MyModule"
  it should "test Encoder83 en is false" in {
    test(new encoder83){ c =>
      c.io.x.poke("b00010000".U)
      c.io.en.poke(false.B)
      c.io.y.expect("b000".U)
    }
  }

  it should "test all case of Encoder83" in {
    for(i <- 0 until 8){
      val input = scala.math.pow(2,i).toInt
      test(new encoder83){ c =>
        c.io.x.poke(input.U)
        c.io.en.poke(true.B)
        c.io.y.expect(i.U)
      }
    }
  }

  it should "test priority of Encoder83" in {
    test(new encoder83){ c =>
      c.io.x.poke("b00010010".U)
      c.io.en.poke(true.B)
      c.io.y.expect(4.U)
    }
  }
}