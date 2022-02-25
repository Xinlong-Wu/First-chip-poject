import chisel3.{fromBooleanToLiteral, fromIntToLiteral}
import chisel3.tester.{testableClock, testableData}
import chiseltest.ChiselScalatestTester
import org.scalatest.freespec.AnyFreeSpec

class CounterSpec extends AnyFreeSpec with ChiselScalatestTester{
  "test coounter" in {
    test(new Counter(8)){ c =>
      c.io.en.poke(true.B)
      c.io.out.expect(0.U)
      for(i <- 0 to 10){
        c.clock.step(1)
        c.io.out.expect((i+1).U)
      }
    }
  }
}
