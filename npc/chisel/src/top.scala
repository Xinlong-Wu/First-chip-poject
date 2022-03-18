import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val sw = IO(Input(UInt(16.W)))
  val ps2_clk =IO(Input(UInt(1.W)))
  val ps2_data =IO(Input(UInt(1.W)))
  val ledr =IO(Output(UInt(16.W)))
  val VGA_CLK =IO(Output(Clock()))
  val VGA_HSYNC =IO(Output(UInt(1.W)))
  val VGA_VSYNC =IO(Output(UInt(1.W)))
  val VGA_BLANK_N =IO(Output(UInt(1.W)))
  val VGA_R =IO(Output(UInt(8.W)))
  val VGA_G =IO(Output(UInt(8.W)))
  val VGA_B =IO(Output(UInt(8.W)))
  val seg0 =IO(Output(UInt(8.W)))
  val seg1 =IO(Output(UInt(8.W)))
  val seg2 =IO(Output(UInt(8.W)))
  val seg3 =IO(Output(UInt(8.W)))
  val seg4 =IO(Output(UInt(8.W)))
  val seg5 =IO(Output(UInt(8.W)))
  val seg6 =IO(Output(UInt(8.W)))
  val seg7 =IO(Output(UInt(8.W)))

  seg0 := "b11111111".U
  seg1 := "b11111111".U
  seg2 := "b11111111".U
  seg3 := "b11111111".U
  seg4 := "b11111111".U
  seg5 := "b11111111".U
  seg6 := "b11111111".U
  seg7 := "b11111111".U
  ledr := 0.U(16.W)
  VGA_CLK := clock
  VGA_HSYNC := 0.U(1.W)
  VGA_VSYNC := 0.U(1.W)
  VGA_BLANK_N := 0.U(1.W)
  VGA_R := 0.U(8.W)
  VGA_G := 0.U(8.W)
  VGA_B := 0.U(8.W)

  val pc_reg = Module(new PC(width))

  val gpr = Module(new GPR(width))
  gpr.io.id := DontCare
  gpr.io.wenable := DontCare
  gpr.io.wdata := DontCare
  gpr.io.rdata := DontCare

//  val ifu = Module(new IFU(width))
//  ifu.io.

}
