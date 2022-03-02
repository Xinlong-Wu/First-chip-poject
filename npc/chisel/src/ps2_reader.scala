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
    val ps2data = Wire(UInt(8.W))
    io.outdata := io.data
    ps2data := io.data

    val ascii_data = Wire(UInt(8.W))

    val scancode2ascii = Module(new scancode2ascii())
    scancode2ascii.io.scan_code := ps2data
    ascii_data := scancode2ascii.io.ascii

    printf(p"got $ps2data ascii: $ascii_data\n")

    io.finish := 1.U
  }

  when(doen === true.B){
    io.finish := 0.U
  }
}
