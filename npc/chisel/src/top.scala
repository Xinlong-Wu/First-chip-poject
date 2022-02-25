import Chisel.Cat
import chisel3._

class top extends Module{
  val io = IO(new Bundle {
    val clk = Input(UInt(1.W))
    val rst = Input(UInt(1.W))
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

  val light = Module(new light())
  light.io.clk := io.clk
  light.io.rst := io.rst
  io.ledr := Cat(light.io.led, "b111".U)

  io.VGA_CLK := io.clk

  val h_addr = Wire(UInt(10.W))
  val v_addr = Wire(UInt(10.W))
  val vga_data = Wire(UInt(10.W))

  val my_vga_ctrl = Module(new vga_ctrl)
  my_vga_ctrl.io.pclk := io.clk
  my_vga_ctrl.io.reset := io.rst
  vga_data := my_vga_ctrl.io.vga_data
  h_addr := my_vga_ctrl.io.h_addr
  v_addr := my_vga_ctrl.io.v_addr
  io.VGA_HSYNC := my_vga_ctrl.io.hsync
  io.VGA_VSYNC := my_vga_ctrl.io.vsync
  io.VGA_BLANK_N := my_vga_ctrl.io.valid
  io.VGA_R := my_vga_ctrl.io.vga_r
  io.VGA_G := my_vga_ctrl.io.vga_g
  io.VGA_B := my_vga_ctrl.io.vga_b

  val my_keyboard = Module(new ps2_keyboard)
  my_keyboard.io.clk := io.clk
  my_keyboard.io.resetn := ~io.rst
  my_keyboard.io.ps2_clk := io.ps2_clk
  my_keyboard.io.ps2_data := io.ps2_data

  val my_vmem = Module(new vmem)
  my_vmem.io.h_addr := h_addr
  my_vmem.io.v_addr := v_addr
  vga_data := my_vmem.io.vga_data
}
