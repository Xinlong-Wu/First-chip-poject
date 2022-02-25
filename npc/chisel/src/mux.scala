import chisel3._
import chisel3.util.MuxLookup

class mux24 extends Module{
  val io = IO(new Bundle() {
    val y = Input(UInt(2.W))
    val x0 = Input(UInt(2.W))
    val x1 = Input(UInt(2.W))
    val x2 = Input(UInt(2.W))
    val x3 = Input(UInt(2.W))
    val f = Output(UInt(2.W))
  })
  io.f := MuxLookup(io.y, 0.U, Array("b00".U -> io.x0, "b01".U -> io.x1, "b10".U -> io.x2, "b11".U -> io.x3 ))
}

class mux38 extends Module{
  val io = IO(new Bundle() {
    val y = Input(UInt(3.W))
    val x0 = Input(UInt(3.W))
    val x1 = Input(UInt(2.W))
    val x2 = Input(UInt(2.W))
    val x3 = Input(UInt(2.W))
    val f = Output(UInt(2.W))
  })
  io.f := MuxLookup(io.y, 0.U, Array("b00".U -> io.x0, "b01".U -> io.x1, "b10".U -> io.x2, "b11".U -> io.x3 ))
}
