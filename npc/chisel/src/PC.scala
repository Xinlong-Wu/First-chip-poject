import chisel3._

class PC(width: Int) extends Module {
  val io = IO(new Bundle() {
    val pc_addr = Output(UInt(width.W))
    val pc_re = Output(Bool())
  })

  val PC_reg = RegInit(UInt(width.W),"h80000000".U)

  PC_reg := PC_reg + 4.U;

  io.pc_addr := PC_reg;
  io.pc_re := Mux(reset.asBool,false.B, true.B);
}
