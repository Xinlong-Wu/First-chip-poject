import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class cmdCtrlSpec extends AnyFreeSpec with ChiselScalatestTester {
  "test " in {
    test(new cmd_ctrl()) { c =>
      for(v_addr <- 0 until 480){
        for(h_addr <- 0 until 640){
          c.io.h_addr.poke(h_addr.U)
          c.io.v_addr.poke(h_addr.U)
          c.clock.step(1)
        }
      }
    }
  }
}