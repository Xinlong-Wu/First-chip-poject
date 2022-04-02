import chisel3._

class IF_ID(width: Int) extends Module {
  val io = IO(new Bundle() {
    val if_pc = Input(UInt(width.W))
    val if_inst = Input(UInt(32.W))
    val id_pc = Output(UInt(width.W))
    val id_inst = Output(UInt(32.W))
  })

  val buff_pc = RegInit(UInt(width.W),"h0".U)
  val buff_inst = RegInit(UInt(32.W),"h0".U)

  buff_pc := io.if_pc
  buff_inst := io.if_inst

  io.id_pc := buff_pc
  io.id_inst := buff_inst
}
