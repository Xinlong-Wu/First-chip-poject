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
  io.pc_waddr := Mux(io.wb_pc, io.wdata_i, io.pc_addr+4.U)

  io.we := io.rf_wb
  io.wid := io.rd_id
  io.wdata_o := Mux(io.wb_pc, io.wdata_i, io.pc_addr+4.U)

  printf("[WBU]: rd=0x%x\n",io.rd_id)
  printf("[WBU]: pc=0x%x\n",io.pc_waddr)
}
