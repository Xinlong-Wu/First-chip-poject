import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class GPR_tester extends AnyFreeSpec with ChiselScalatestTester {
  "test GPR reset" in {
    test(new GPR(64)) { c =>
      c.reset.poke(true.B)
      c.clock.step(1)
      c.reset.poke(false.B)
      for (i <- 1 until 32){
        c.io.wid.poke(i.U)
        c.io.we.poke(false.B)
        c.io.wdata.poke(0.U)
        c.io.rid1.poke(i.U)
        c.io.rdata1.expect(0.U)
      }
    }
  }

  "test GPR order of reset and write" in {
    test(new GPR(64)) { c =>
      c.reset.poke(true.B)
      c.clock.step(1)
      c.io.we.poke(true.B)
      for (i <- 1 until 32){
        val data = scala.util.Random.nextInt(100).abs
        c.io.wid.poke(i.U)
        c.io.wdata.poke(data.U)
        c.clock.step(1)
        c.io.rid1.poke(i.U)
        c.io.rdata1.expect(0.U)
      }
    }
  }

  "test GPR write/write" in {
    test(new GPR(64)) { c =>
      c.reset.poke(true.B)
      c.clock.step(1)
      c.reset.poke(false.B)
      for (i <- 1 until 32){
        val data = scala.util.Random.nextInt(100).abs
        c.io.wid.poke(i.U)
        c.io.we.poke(true.B)
        c.io.wdata.poke(data.U)
        c.clock.step(1)
        c.io.we.poke(false.B)
        c.io.rid2.poke(i.U)
        c.io.rdata2.expect(data.U)
      }
    }
  }

  "test X0 gpr" in {
    test(new GPR(64)) { c =>
      c.reset.poke(true.B)
      c.clock.step(1)
      c.reset.poke(false.B)
      for (i <- 0 until 32){
        val data = scala.util.Random.nextInt(100).abs
        c.io.wid.poke(0.U)
        c.io.we.poke(true.B)
        c.io.wdata.poke(data.U)
        c.clock.step(1)
        c.io.we.poke(false.B)
        c.io.rid2.poke(0.U)
        c.io.rdata2.expect(0.U)
      }
    }
  }
}