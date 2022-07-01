import Chisel.Cat
import Decoder.{ALUOpType, FuType, LSUOpType}
import chisel3._

class WBU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val inst_info = Flipped(new InstInfo())
    val dmem_wen = Output(Bool())

    val rd_id = Input(UInt(5.W))
    val wdata_i = Input(UInt(width.W))
    val pc_addr = Input(UInt(width.W))

    val we = Output(Bool())
    val wid = Output(UInt(5.W))
    val wdata_o = Output(UInt(width.W))
    val pc_we = Output(Bool())
    val pc_waddr = Output(UInt(width.W))
  })
  io.pc_we := (io.inst_info.fuop === FuType.jmp)
  io.pc_waddr := Cat(io.wdata_i(width-1,1), 0.U)

  io.we := io.inst_info.rfwen
  io.wid := io.rd_id
  io.wdata_o := Mux(io.inst_info.fuop === FuType.jmp,io.pc_addr+4.U, io.wdata_i)

  io.dmem_wen := (io.inst_info.fuop === FuType.alu && io.inst_info.aluty === LSUOpType.sd)

  printf("[WBU]: wdata_i=0x%x\n",io.wdata_i)
}
