import Decoder.{ALUOpType, FuType}
import chisel3._
import chiseltest.{ChiselScalatestTester, testableData, testableClock}
import org.scalatest.freespec.AnyFreeSpec

class IDU_tester extends AnyFreeSpec with ChiselScalatestTester {
  "test Decode ADDI" in {
    test(new IDU(64)) { c =>
      c.reset.poke(false.B)
      c.io.reg1_data_i.poke("h3".U(64.W))

      c.io.inst_data.poke("b0000000_00011_00001_000_00001_00100_11".U)

      c.clock.step(1)

      c.io.reg1_re.expect(true.B)
      c.io.reg2_re.expect(false.B)
      c.io.rd_id.expect("b00001".U)
      c.io.reg1_rid.expect("b00001".U)
      c.io.reg2_rid.expect("b00000".U)
      c.io.reg1_data_o.expect("h3".U(64.W))

      c.io.imm_data.expect("b0000000_00011".U)
      c.io.inst_info.fuop.expect(FuType.alu)
      c.io.inst_info.aluty.expect(ALUOpType.addi)
      c.io.inst_info.rfwen.expect(true.B)
    }
  }

  "test Decode AUIPC" in {
    test(new IDU(64)) { c =>
      c.reset.poke(false.B)
      c.io.pc_addr.poke("h80000000".U(64.W))
      c.io.reg1_data_i.poke("h3".U(64.W))

      c.io.inst_data.poke("b0000000_00000_00000_111_00001_00101_11".U)

      c.clock.step(1)

      c.io.reg1_re.expect(true.B)
      c.io.reg2_re.expect(false.B)
      c.io.rd_id.expect("b00001".U)
      c.io.reg1_rid.expect("b00000".U)
      c.io.reg2_rid.expect("b00000".U)
      c.io.reg1_data_o.expect("h80000000".U(64.W))
      c.io.imm_data.expect("b0000000_00000_00000_111".U)
      c.io.inst_info.fuop.expect(FuType.alu)
      c.io.inst_info.aluty.expect(ALUOpType.addi)
      c.io.inst_info.rfwen.expect(true.B)
    }
  }
}