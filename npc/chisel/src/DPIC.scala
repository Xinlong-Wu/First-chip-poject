import chisel3._

class DPIC extends BlackBox{
  val io = IO(new Bundle {
    val is_ebreak = Input(UInt(1.W))
    val result = Input(UInt(32.W))
    val unimp = Input(UInt(1.W))
  })
}
