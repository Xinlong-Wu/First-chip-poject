import chisel3._

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val data = Input(UInt(8.W))
    val en = Input(Bool())
    val overflow = Input(Bool())
    val finish = Output(Bool())
//    val outdata = Output(UInt(8.W))
  })
  io.finish := ~io.en

  val ps2data = Wire(UInt(8.W))
  ps2data := io.data

  when(io.overflow){
    printf(p"over flow!!\n")
  }

  when(clock.asBool){
    printf(p"got $ps2data \n")
  }
}
