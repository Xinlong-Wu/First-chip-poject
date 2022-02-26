import chisel3._

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val data = Input(UInt(8.W))
    val overflow = Input(Bool())
    val finish = Output(Bool())
  })

  io.finish := DontCare

  when(reset.asBool){
    printf(p"reset!\n")
  }

  when(io.overflow){
    printf(p"over flow!!\n")
  }

  when(clock.asBool){
    printf(p"got $io \n")
    io.finish := true.B
  }
}
