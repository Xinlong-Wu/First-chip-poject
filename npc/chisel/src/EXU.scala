import Decoder.{ALUOpType, FuType, JumpOpType}
import chisel3._
import chisel3.util.MuxCase

class EXU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val fuop = Input(UInt(8.W))
    val aluty = Input(UInt(3.W))

    val reg1_re = Input(Bool())
    val reg2_re = Input(Bool())
    val reg1_data = Input(UInt(width.W))
    val reg2_data = Input(UInt(width.W))
    val imm_data = Input(UInt(width.W))

    val wdata = Output(UInt(width.W))
  })

  io.wdata := MuxCase(0.U, Array(
    (io.fuop === FuType.alu && (io.aluty === ALUOpType.addi)) -> (io.reg1_data + io.imm_data),
    (io.fuop === FuType.jmp && (io.aluty === JumpOpType.jal)) -> (io.reg1_data + io.imm_data),
    reset.asBool -> 0.U
  ))

  printf("[EXU]: src1=0x%x, src2=0x%x, imm=0x%x, rd=0x%x\n",io.reg1_data, io.reg2_data,io.imm_data,io.wdata)

}
