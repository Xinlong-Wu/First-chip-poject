import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class PC_tester extends AnyFreeSpec with ChiselScalatestTester {
  "test PC increase" in {
    test(new PC(64)) { c =>
      for(i <- 0 until 10) {
        c.io.pc_addr.expect((4 * i).U + "0x80000000".U)
        c.clock.step(1)
      }
    }
  }
}