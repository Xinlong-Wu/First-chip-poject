import chisel3._

class GPR(width: Int) extends Module {
  val io = IO(new Bundle() {
    val wid = Input(UInt(5.W))
    val we = Input(Bool())
    val wdata = Input(UInt(width.W))
    val rid1 = Input(UInt(5.W))
    val re1 = Input(Bool())
    val rid2 = Input(UInt(5.W))
    val re2 = Input(Bool())
    val rdata1 = Output(UInt(width.W))
    val rdata2 = Output(UInt(width.W))
  })
  val GPR = Reg(Vec(32,UInt(width.W)))

  GPR(0) := 0.U(width.W)
  for(i <- 1 to 31){
    GPR(i) := Mux(reset.asBool, 0.U(width.W), Mux(io.we && io.wid === i.U, io.wdata, GPR(io.wid)))
  }

  io.rdata1 := Mux(reset.asBool || io.re1, 0.U(width.W), GPR(io.rid1))
  io.rdata2 := Mux(reset.asBool || io.re2, 0.U(width.W), GPR(io.rid2))
}
