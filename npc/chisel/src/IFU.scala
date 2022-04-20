import chisel3._

class IFU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val pc_addr = Input(UInt(width.W))
    val pc_we = Input(Bool())
    val inst_data = Output(UInt(32.W))
    val inst_re = Output(Bool())
  })

}
