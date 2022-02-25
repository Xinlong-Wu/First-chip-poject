import Chisel.Cat
import chisel3._

class bcd7seg extends Module{
  val io = IO(new Bundle() {
    val num = Input(UInt(4.W))
    val en = Input(Bool())
    val HEX = Output(UInt(8.W))
  })
  val encode = Wire(UInt(8.W))
  encode := "b11111111".U
  io.HEX := "b11111111".U

//  when(io.en){
//    when(io.num === 0.U){
//      io.HEX := Cat(encode(7, 6), 0.U(6.W))
//    }.elsewhen(io.num === 1.U){
//      io.HEX := Cat(encode(7, 3), 0.U(2.W), encode(0))
//    }.elsewhen(io.num === 2.U){
//      io.HEX := Cat(encode(7), 0.U(1.W), encode(5), 0.U(2.W), encode(2), 0.U(2.W))
//    }.elsewhen(io.num === 3.U){
//      io.HEX := Cat(encode(7), 0.U(1.W), encode(5, 4), 0.U(4.W))
//    }.elsewhen(io.num === 4.U){
//      io.HEX := Cat(encode(7), 0.U(2.W), encode(4, 3), 0.U(2.W), encode(0))
//    }.elsewhen(io.num === 5.U){
//      io.HEX := Cat(encode(7), 0.U(2.W), encode(4), 0.U(2.W), encode(1), 0.U(1.W))
//    }.elsewhen(io.num === 6.U){
//      io.HEX := Cat(encode(7), 0.U(5.W), encode(1), 0.U(1.W))
//    }.elsewhen(io.num === 7.U){
//      io.HEX := Cat(encode(7, 3), 0.U(3.W))
//    }.elsewhen(io.num === 8.U){
//      io.HEX := Cat(encode(7, 6), 0.U(6.W))
//    }.elsewhen(io.num === 9.U){
//      io.HEX := Cat(encode(7), 0.U(2.W), encode(4), 0.U(4.W))
//    }
//  }
}
