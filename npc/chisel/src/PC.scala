import chisel3._

class PC(width: Int) extends Module {
  val io = IO(new Bundle() {
    val pc_we = Input(Bool())
    val pc_waddr = Input(UInt(width.W))

    val pc_addr = Output(UInt(width.W))
    val pc_re = Output(Bool())
  })

  val PC_reg = RegInit(UInt(width.W),"h80000000".U)

  PC_reg := Mux(io.pc_we,io.pc_waddr,PC_reg + 4.U);

  io.pc_addr := PC_reg;
  io.pc_re := Mux(reset.asBool || io.pc_we,false.B, true.B);
}
