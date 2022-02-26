import Chisel.Cat
import chisel3._
import chisel3.util.Counter
import chisel3.util.experimental.loadMemoryFromFile

class top extends RawModule{
  val clock = IO(Input(Clock()))
  val reset = IO(Input(UInt(1.W)))
  val sw = IO(Input(UInt(16.W)))
  val ps2_clk = IO(Input(UInt(1.W)))
  val ps2_data = IO(Input(UInt(1.W)))
  val ledr = IO(Output(UInt(16.W)))
  val VGA_CLK = IO(Output(UInt(1.W)))
  val VGA_HSYNC = IO(Output(UInt(1.W)))
  val VGA_VSYNC = IO(Output(UInt(1.W)))
  val VGA_BLANK_N = IO(Output(UInt(1.W)))
  val VGA_R = IO(Output(UInt(8.W)))
  val VGA_G = IO(Output(UInt(8.W)))
  val VGA_B = IO(Output(UInt(8.W)))
  val seg0 = IO(Output(UInt(8.W)))
  val seg1 = IO(Output(UInt(8.W)))
  val seg2 = IO(Output(UInt(8.W)))
  val seg3 = IO(Output(UInt(8.W)))
  val seg4 = IO(Output(UInt(8.W)))
  val seg5 = IO(Output(UInt(8.W)))
  val seg6 = IO(Output(UInt(8.W)))
  val seg7 = IO(Output(UInt(8.W)))

  seg0 := "b11111111".U
  seg1 := "b11111111".U
  seg2 := "b11111111".U
  seg3 := "b11111111".U
  seg4 := "b11111111".U
  seg5 := "b11111111".U
  seg6 := "b11111111".U
  seg7 := "b11111111".U
  ledr := 0.U(16.W)
  VGA_CLK := 0.U(1.W)
  VGA_HSYNC := 0.U(1.W)
  VGA_VSYNC := 0.U(1.W)
  VGA_BLANK_N := 0.U(1.W)
  VGA_R := 0.U(8.W)
  VGA_G := 0.U(8.W)
  VGA_B := 0.U(8.W)

  val flowlight = Module(new light())
  flowlight.io.clk := clock
  flowlight.io.rst := reset
  ledr := Cat(flowlight.io.led, 0.U(3.W))

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
    seg0 := num1.io.HEX

    val num2 = Module(new bcd7seg())
    num2.io.en := 1.U
    num2.io.num := ((counterRes % 100.U(8.W))/10.U(8.W))(3, 0)
    seg1 := num2.io.HEX

    val num3 = Module(new bcd7seg())
    num3.io.en := 1.U
    num3.io.num := (counterRes / 100.U(8.W))(3, 0)
    seg2 := num3.io.HEX

    val my_keyboard = Module(new ps2_keyboard())
    my_keyboard.io.clk := clock.asUInt
    my_keyboard.io.resetn := ~reset
    my_keyboard.io.ps2_clk := ps2_clk
    my_keyboard.io.ps2_data := ps2_data

    val my_vga_ctrl = Module(new vga_ctrl())
    my_vga_ctrl.io.pclk := clock.asUInt
    my_vga_ctrl.io.reset := reset
    my_vga_ctrl.io.vga_data := vga_data
    h_addr := my_vga_ctrl.io.h_addr
    v_addr := my_vga_ctrl.io.v_addr
    VGA_HSYNC := my_vga_ctrl.io.hsync
    VGA_VSYNC := my_vga_ctrl.io.vsync
    VGA_BLANK_N := my_vga_ctrl.io.valid
    VGA_R := my_vga_ctrl.io.vga_r
    VGA_G := my_vga_ctrl.io.vga_g
    VGA_B := my_vga_ctrl.io.vga_b
  }

  val mem = Module(new vmem())
  mem.clock := clock
  mem.reset := reset
  mem.io.h_addr := h_addr
  mem.io.v_addr := v_addr(8,0)
  vga_data := mem.io.vga_data

}
