import Chisel.Cat
import chisel3._

class chiseltop extends Module{
  val io = IO(new Bundle {
    val sw = Input(UInt(16.W))
    val ps2_clk = Input(UInt(1.W))
    val ps2_data = Input(UInt(1.W))
    val ledr = Output(UInt(16.W))
    val VGA_CLK = Output(UInt(1.W))
    val VGA_HSYNC = Output(UInt(1.W))
    val VGA_VSYNC = Output(UInt(1.W))
    val VGA_BLANK_N = Output(UInt(1.W))
    val VGA_R = Output(UInt(8.W))
    val VGA_G = Output(UInt(8.W))
    val VGA_B = Output(UInt(8.W))
    val seg0 = Output(UInt(8.W))
    val seg1 = Output(UInt(8.W))
    val seg2 = Output(UInt(8.W))
    val seg3 = Output(UInt(8.W))
    val seg4 = Output(UInt(8.W))
    val seg5 = Output(UInt(8.W))
    val seg6 = Output(UInt(8.W))
    val seg7 = Output(UInt(8.W))
  })

  io.seg0 := "b11111111".U
  io.seg1 := "b11111111".U
  io.seg2 := "b11111111".U
  io.seg3 := "b11111111".U
  io.seg4 := "b11111111".U
  io.seg5 := "b11111111".U
  io.seg6 := "b11111111".U
  io.seg7 := "b11111111".U
  io.ledr := 0.U(16.W)
  io.VGA_CLK := 0.U(1.W)
  io.VGA_HSYNC := 0.U(1.W)
  io.VGA_VSYNC := 0.U(1.W)
  io.VGA_BLANK_N := 0.U(1.W)
  io.VGA_R := 0.U(8.W)
  io.VGA_G := 0.U(8.W)
  io.VGA_B := 0.U(8.W)
}
