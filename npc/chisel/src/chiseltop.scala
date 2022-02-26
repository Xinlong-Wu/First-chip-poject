import Chisel.Cat
import chisel3._
import chisel3.util.Counter

class chiseltop extends RawModule{
  val clock = IO(Input(Clock()))
  val reset = IO(Input(UInt(1.W)))
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

  val flowlight = Module(new light())
  flowlight.io.clk := clock
  flowlight.io.rst := reset
  io.ledr := Cat(flowlight.io.led, 0.U(3.W))

  val h_addr = Wire(UInt(10.W))
  val v_addr = Wire(UInt(10.W))
  val vga_data = Wire(UInt(24.W))


  withClockAndReset(clock, reset.asBool){
    val (_, clk_1s) = Counter(true.B, 24999999)

    val counterRes = RegInit(UInt(8.W),0.U(8.W))
    when(clk_1s){
      counterRes := counterRes + 1.U(8.W)
    }

    val num1 = Module(new bcd7seg())
    num1.io.en := 1.U
    num1.io.num := (counterRes % 10.U(8.W))(3, 0)
    io.seg0 := num1.io.HEX

    val num2 = Module(new bcd7seg())
    num2.io.en := 1.U
    num2.io.num := ((counterRes % 100.U(8.W))/10.U(8.W))(3, 0)
    io.seg1 := num2.io.HEX

    val num3 = Module(new bcd7seg())
    num3.io.en := 1.U
    num3.io.num := (counterRes / 100.U(8.W))(3, 0)
    io.seg2 := num3.io.HEX

    val my_keyboard = Module(new ps2_keyboard())
    my_keyboard.io.clk := clock.asUInt
    my_keyboard.io.resetn := ~reset
    my_keyboard.io.ps2_clk := io.ps2_clk
    my_keyboard.io.ps2_data := io.ps2_data

    val my_vga_ctrl = Module(new vga_ctrl())
    my_vga_ctrl.io.pclk := clock.asUInt
    my_vga_ctrl.io.reset := reset
    my_vga_ctrl.io.vga_data := vga_data
    h_addr := my_vga_ctrl.io.h_addr
    v_addr := my_vga_ctrl.io.v_addr
    io.VGA_HSYNC := my_vga_ctrl.io.hsync
    io.VGA_VSYNC := my_vga_ctrl.io.vsync
    io.VGA_BLANK_N := my_vga_ctrl.io.valid
    io.VGA_R := my_vga_ctrl.io.vga_r
    io.VGA_G := my_vga_ctrl.io.vga_g
    io.VGA_B := my_vga_ctrl.io.vga_b
  }

//  val mem = Module(new vmem())
//  mem.clock := clock
//  mem.reset := reset
//  mem.io.h_addr := h_addr
//  mem.io.v_addr := v_addr(8,0)
//  vga_data := mem.io.vga_data

}
