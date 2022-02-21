package decoder

import chisel3._

class decoder extends Module{
  var io = IO(new Bundle() {
    var En = Input(Bool())
    var x = Input(UInt(2.W))

    var y = Output(UInt(4.W))
  })

  io.y := 0.U

  when(io.En){
    when(io.x === "d0".U){
      io.y := "b0001".U
    }.elsewhen(io.x === "d1".U){
      io.y := "b0010".U
    }.elsewhen(io.x === "d2".U){
      io.y := "b0100".U
    }.elsewhen(io.x === "d3".U){
      io.y := "b1000".U
    }
  }
}

object emitDecoder extends App{

}