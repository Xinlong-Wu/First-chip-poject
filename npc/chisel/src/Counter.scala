import chisel3._

class Counter(bitWidth: Int) extends Module{
  val io = IO(new Bundle() {
    val en = Input(Bool())
    val out = Output(UInt(bitWidth.W))
  })
  var reg = RegInit(0.U(bitWidth.W))
  when(io.en){
    reg := reg + 1.U
  }

  io.out := reg
}
