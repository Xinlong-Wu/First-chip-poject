import chisel3._
import firrtl.Utils.False

class ps2_reader extends Module {
  val io = IO(new Bundle() {
    val readClk = Input(Clock())
    val data = Input(UInt(8.W))
    val en = Input(Bool())
    val finish = Output(Bool())
    val outdata = Output(UInt(8.W))
  })

  val doen = RegInit(false.B)
  doen := io.finish

  withClock(io.readClk){
    io.outdata := io.data
    io.finish := 1.U
  }

  when(doen === true.B){
    io.finish := 0.U
  }
}
