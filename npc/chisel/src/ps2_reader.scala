import chisel3._

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val data = Input(UInt(8.W))
    val finish = Output(Bool())
  })
  io.finish := DontCare

  when(clock.asBool){
    printf("c")
  }
}
