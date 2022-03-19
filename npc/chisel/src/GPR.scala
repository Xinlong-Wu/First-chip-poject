import chisel3._

class GPR(width: Int) extends Module {
  val io = IO(new Bundle() {
    val id = Input(UInt(5.W))
    val wenable = Input(Bool())
    val wdata = Input(UInt(width.W))
    val rdata = Output(UInt(width.W))
  })
  val GPR = Reg(Vec(32,UInt(width.W)))

  for(i <- 0 to 31){
    GPR(i) := Mux(reset.asBool, 0.U(width.W), Mux(io.wenable && io.id === i.U, io.wdata, GPR(io.id)))
  }

  io.rdata := GPR(io.id)
}
