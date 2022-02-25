import chisel3._
import chiseltest.{ChiselScalatestTester, testableClock, testableData}
import org.scalatest.freespec.AnyFreeSpec

class AdderTest extends AnyFreeSpec with ChiselScalatestTester{

  "test FullAdder" in {
    test(new FullAdder){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(1.U)
      c.io.overflow.expect(0.U)

      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(0.U)
      c.io.overflow.expect(1.U)

      c.io.in1.poke(0.U)
      c.io.in2.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.cin.poke(0.U)
      c.io.out.expect(0.U)
      c.io.overflow.expect(0.U)
    }
  }

  "test 4 bit Adder" in {
    test(new RippleCarryAdder(4)){ c =>
      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1100".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b1111".U)
      c.io.overflow.expect(0.U)

      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1110".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b0001".U)
      c.io.overflow.expect(1.U)
    }

    test(new CarryLookAheadAdder(4)){ c =>
      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1100".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b1111".U)
      c.io.overflow.expect(0.U)

      c.io.in1.poke("b0011".U)
      c.io.in2.poke("b1110".U)
      c.io.cin.poke(0.U)
      c.io.out.expect("b0001".U)
      c.io.overflow.expect(1.U)
    }
  }

  "test 32 bit Add&minus" in {
    test(new Arithmetic(32)){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.minus.poke(false.B)
      c.io.res.expect(2.U)
      c.io.overflow.expect(0.U)
      c.io.zero.expect(0.B)
    }

    test(new Arithmetic(32)){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.minus.poke(true.B)

      c.io.overflow.expect(0.U)
      c.io.zero.expect(1.B)
    }

    test(new Arithmetic(32)){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(2.U)
      c.io.minus.poke(true.B)

      c.io.overflow.expect(1.U)
      c.io.zero.expect(0.B)
    }
  }

  "test zero and up/down overflow" in {

    test(new Arithmetic(1)){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.minus.poke(false.B)
      c.io.res.expect(0.U)
      c.io.overflow.expect(1.U)
      c.io.zero.expect(0.B)
    }

    test(new Arithmetic(1)){c =>
      c.io.in1.poke(1.U)
      c.io.in2.poke(1.U)
      c.io.minus.poke(true.B)

      c.io.overflow.expect(0.U)
      c.io.zero.expect(1.B)
    }
  }
}