import Decoder.{ALUOpType, FuType}
import chisel3._
import chisel3.util.MuxCase

class EXU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val fuop = Input(UInt(8.W))
    val aluty = Input(UInt(3.W))

    val reg1_re = Input(Bool())
    val reg2_re = Input(Bool())
    val rd_we = Input(Bool())
    val rd_id = Input(UInt(5.W))
    val reg1_data = Input(UInt(width.W))
    val reg2_data = Input(UInt(width.W))
    val imm_data = Input(UInt(width.W))

    val wdata = Output(UInt(width.W))
    val wrd_id = Output(UInt(5.W))
    val wrd_en = Output(Bool())
  })

  io.wrd_id := io.rd_id
  io.wrd_en := io.rd_we

  io.wdata := MuxCase(0.U, Array(
    (io.fuop === FuType.alu && io.aluty === ALUOpType.addi) -> (io.reg1_data + io.imm_data),
    reset.asBool -> 0.U
  ))

}
