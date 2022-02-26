import Chisel.{Cat, switch}
import chisel3._
import chisel3.util.is

class bcd7seg extends Module{
  val io = IO(new Bundle() {
    val num = Input(UInt(4.W))
    val en = Input(Bool())
    val HEX = Output(UInt(8.W))
  })
  val encode = Wire(UInt(8.W))
  encode := "b11111111".U
  io.HEX := "b11111111".U

  when(io.en){
    switch(io.num) {
      is(0.U) {
        io.HEX := Cat(0.U(6.W), encode(7, 6))
      }
      is(1.U) {
        io.HEX := Cat(encode(0), 0.U(2.W), encode(7, 3))
      }
      is(2.U) {
        io.HEX := Cat(0.U(2.W), encode(2), 0.U(2.W), encode(5), 0.U(1.W), encode(7))
      }
      is(3.U) {
        io.HEX := Cat(0.U(4.W), encode(5, 4), 0.U(1.W), encode(7))
      }
      is(4.U) {
        io.HEX := Cat(encode(0), 0.U(2.W), encode(4, 3), 0.U(2.W), encode(7))
      }
      is(5.U) {
        io.HEX := Cat(0.U(1.W), encode(1), 0.U(2.W), encode(4), 0.U(2.W), encode(7))
      }
      is(6.U) {
        io.HEX := Cat(0.U(1.W), encode(1), 0.U(5.W), encode(7))
      }
      is(7.U) {
        io.HEX := Cat(0.U(3.W), encode(7, 3))
      }
      is(8.U) {
        io.HEX := Cat(0.U(7.W), encode(7))
      }
      is(9.U) {
        io.HEX := Cat(0.U(4.W), encode(4), 0.U(2.W), encode(7))
      }
    }
  }
}
