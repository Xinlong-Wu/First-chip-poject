import chisel3._
import firrtl.Utils.False

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val readClk = Input(Clock())
    val data = Input(UInt(8.W))
    val en = Input(Bool())
    val finish = Output(Bool())
//    val outdata = Output(UInt(8.W))
  })

  withClock(io.readClk){
    val ps2data = Wire(UInt(8.W))
    ps2data := io.data

    printf(p"got $io \n")
    io.finish := 1.U
  }
}
