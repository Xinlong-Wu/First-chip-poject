package mux

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util

class MUX extends Module{
  var io = IO(new Bundle() {
    var a = Input(UInt(1.W))
    var b = Input(UInt(1.W))
    var s = Input(UInt(1.W))
    var y = Input(UInt(1.W))
  })

  when(io.s === 0.U){
    io.y := io.a
  }.otherwise{
    io.y := io.b
  }

}

object emitMUX extends App{
  (new ChiselStage).emitVerilog((new MUX), Array("--target-dir", "generated"))
}

