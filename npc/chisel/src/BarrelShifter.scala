import chisel3._
import chisel3.util.{Cat, Fill}

class BarrelShifter(bitWidth: Int, shaftAbelBit: Int) extends Module {
  val io = IO(new Bundle() {
    val din = Input(UInt(bitWidth.W))
    val shamt = Input(UInt(shaftAbelBit.W))
    val LR = Input(Bool())
    val AL = Input(Bool())
    val dout = Output(UInt(bitWidth.W))
  })

  when((!io.LR) & io.AL){ // Right Arithmetic Shift
    io.dout := (io.din.asSInt >> io.shamt).asUInt
  }.elsewhen((!io.LR) & (!io.AL)){ // Right Logic Shift
    io.dout := io.din >> io.shamt
  }.otherwise{  // Left Arithmetic Shift & Left Logic Shift
    io.dout := io.din << io.shamt
  }

}
