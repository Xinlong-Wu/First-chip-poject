import chisel3._
import com.google.protobuf.BoolValueOrBuilder

class FullAdder extends Module{
  val io = IO(new Bundle() {
    val in1 = Input(UInt(1.W))
    val in2 = Input(UInt(1.W))
    val cin = Input(UInt(1.W))
    val out = Output(UInt(1.W))
    val overflow = Output(UInt(1.W))
  })

  val in1_xor_in2 = io.in1 ^ io.in2
  io.out := in1_xor_in2 ^ io.cin

  val in1_and_in2 = io.in1 & io.in2
  io.overflow := (in1_xor_in2 & io.cin) | in1_and_in2
}

class RippleCarryAdder(bitWidth: Int) extends Module{
  val io = IO(new Bundle() {
    val in1 = Input(UInt(bitWidth.W))
    val in2 = Input(UInt(bitWidth.W))
    val cin = Input(UInt(1.W))
    val out = Output(UInt(bitWidth.W))
    val overflow = Output(UInt(1.W))
  })
  val cinVec = Wire(Vec(bitWidth+1,UInt(1.W)))
  val res = Wire(Vec(bitWidth,UInt(1.W)))
  val FAs = Array.fill(bitWidth)(Module(new FullAdder()).io)
  cinVec(0) := io.cin

  for(i <- 0 until bitWidth){
    FAs(i).in1 := io.in1(i)
    FAs(i).in2 := io.in2(i)
    FAs(i).cin := cinVec(i)
    res(i) := FAs(i).out
    cinVec(i+1) := FAs(i).overflow
  }

  io.overflow := cinVec(bitWidth)
  io.out := res.asUInt
}

class CarryLookAheadAdder(bitWidth: Int) extends Module{
  val io = IO(new Bundle() {
    val in1 = Input(UInt(bitWidth.W))
    val in2 = Input(UInt(bitWidth.W))
    val cin = Input(UInt(1.W))
    val out = Output(UInt(bitWidth.W))
    val overflow = Output(UInt(1.W))
  })
  val G = Wire(Vec(bitWidth,UInt(1.W)))
  val P = Wire(Vec(bitWidth,UInt(1.W)))
  val C = Wire(Vec(bitWidth+1,UInt(1.W)))
  C(0) := io.cin

  G := (io.in1 & io.in2).asBools
  P := (io.in1 | io.in2).asBools
  for(i <- 0 until bitWidth){
    C(i+1) := G(i) | (P(i) & C(i))
  }

  val res = Wire(Vec(bitWidth,UInt(1.W)))
  val FAs = Array.fill(bitWidth)(Module(new FullAdder()).io)

  for(i <- 0 until bitWidth){
    FAs(i).in1 := io.in1(i)
    FAs(i).in2 := io.in2(i)
    FAs(i).cin := C(i)
    res(i) := FAs(i).out
  }

  io.overflow := C(bitWidth)
  io.out := res.asUInt
}

class Arithmetic(bitWidth: Int) extends Module{
  val io = IO(new Bundle() {
    val in1 = Input(UInt(bitWidth.W))
    val in2 = Input(UInt(bitWidth.W))
    val minus = Input(Bool())
    val res = Output(UInt(bitWidth.W))
    val overflow = Output(UInt(1.W))
    val zero = Output(UInt(1.W))
  })
  val a = Wire(UInt(bitWidth.W))
  val b = Wire(UInt(bitWidth.W))

  a := io.in1
  b := io.in2
  val adder = Module(new CarryLookAheadAdder(bitWidth))

  when(io.minus){
    b := ~io.in2
  }

  adder.io.in1 := a
  adder.io.in2 := b
  adder.io.cin := io.minus.asUInt

  io.res := adder.io.out
  io.overflow := adder.io.overflow ^ io.minus
  io.zero := adder.io.overflow & io.minus
}