import chisel3._

class GPR(width: Int) extends BlackBox {
  val io = IO(new Bundle() {
    val rst = Input(Reset())
    val clk = Input(Clock())
    val wdata = Input(UInt(width.W))
    val waddr = Input(UInt(5.W))
    val wen = Input(Bool())

    val raddr1 = Input(UInt(5.W))
    val ren1 = Input(Bool())
    val raddr2 = Input(UInt(5.W))
    val ren2 = Input(Bool())
    val rdata1 = Output(UInt(width.W))
    val rdata2 = Output(UInt(width.W))
  })
}
