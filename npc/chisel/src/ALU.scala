import chisel3._

class ALU(bitWidth: Int) extends Module{
  val io = IO(new Bundle() {
    val op = Input(UInt(3.W))
    val in1 = Input(UInt(bitWidth.W))
    val in2 = Input(UInt(bitWidth.W))
    val zero = Output(Bool())
    val overflow = Output(Bool())
    val res = Output(UInt(bitWidth.W))
  })
  io.zero := false.B
  io.overflow := false.B
  io.res := 0.U

  val adder = Module(new Arithmetic(bitWidth))
  adder.io.in1 := 0.U
  adder.io.in2 := 0.U
  adder.io.minus := 0.U

  when(io.op === "b000".U){
    adder.io.in1 := io.in1
    adder.io.in2 := io.in2
    adder.io.minus := false.B
    io.res := adder.io.res
    io.overflow := adder.io.overflow
    io.zero := adder.io.zero
  }.elsewhen(io.op === "b001".U){
    adder.io.in1 := io.in1
    adder.io.in2 := io.in2
    adder.io.minus := true.B
    io.res := adder.io.res
    io.overflow := adder.io.overflow
    io.zero := adder.io.zero
  }.elsewhen(io.op === "b010".U){
    io.res := ~io.in1
  }.elsewhen(io.op === "b011".U){
    io.res := io.in1 & io.in2
  }.elsewhen(io.op === "b100".U){
    io.res := io.in1 | io.in2
  }.elsewhen(io.op === "b101".U){
    io.res := io.in1 ^ io.in2
  }.elsewhen(io.op === "b110".U){
    io.res := io.in1 <= io.in2
  }.elsewhen(io.op === "b100".U){
    io.res := io.in1 === io.in2
  }
}
