import chisel3._

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val data = Input(UInt(8.W))
    val overflow = Input(Bool())
    val finish = Output(Bool())
//    val outdata = Output(UInt(8.W))
  })
  val finish = RegInit(1.U(8.W))
  io.finish := finish

  when(io.overflow){
    printf(p"over flow!!\n")
  }

  when(clock.asBool){
    printf(p"got $ps2data \n")
//    io.finish := true.B
  }
}
