package Adder

import chisel3._
import chisel3.stage.ChiselStage

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

object emitFullAdder extends App{
  (new ChiselStage).emitVerilog((new FullAdder), Array("--target-dir", "generated"))
}

class Adder(bitWidth: Int) extends Module{
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
