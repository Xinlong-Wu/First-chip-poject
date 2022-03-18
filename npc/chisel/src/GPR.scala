import chisel3._

class GPR(width: Int) extends Module {
  val io = IO(new Bundle() {
    val id = Input(UInt(5.W))
    val wenable = Input(Bool())
    val wdata = Input(UInt(width.W))
    val rdata = Output(UInt(width.W))
  })

  val GPR = Reg(Vec(32,UInt(width.W)))

  if(reset.asBool == true.B){
    for(i <- 0 to 31){
      GPR(i) := 0.U(width.W)
    }
  }

  if(io.wenable == true.B){
    GPR(io.id) := io.wdata
  }

  io.rdata := GPR(io.id)
}
