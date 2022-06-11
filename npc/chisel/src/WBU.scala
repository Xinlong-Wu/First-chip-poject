import Chisel.Cat
import chisel3._

class WBU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val wb_pc = Input(Bool())
    val rf_wb = Input(Bool())
    val rd_id = Input(UInt(5.W))
    val wdata_i = Input(UInt(width.W))
    val pc_addr = Input(UInt(width.W))

    val we = Output(Bool())
    val wid = Output(UInt(5.W))
    val wdata_o = Output(UInt(width.W))
    val pc_we = Output(Bool())
    val pc_waddr = Output(UInt(width.W))
  })
  io.pc_we := io.wb_pc
  io.pc_waddr := Cat(io.wdata_i(width-1,1), 0.U)

  io.we := io.rf_wb
  io.wid := io.rd_id
  io.wdata_o := Mux(io.wb_pc,io.pc_addr+4.U, io.wdata_i)

  printf("[WBU]: pc_wb=0x%x\n",io.wdata_i)
}
