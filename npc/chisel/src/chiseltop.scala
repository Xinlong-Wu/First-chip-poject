import Chisel.Cat
import chisel3._

class chiseltop extends RawModule{
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(UInt(1.W))
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

  var flowlight = Module(new light())
  flowlight.io.clk := io.clock.asUInt
  flowlight.io.rst := io.reset
  io.ledr := Cat(flowlight.io.led, 0.U(3.W))

  withClockAndReset(io.clock, io.reset.asBool){
    val clk_1s = Reg(UInt(1.W))
    clk_1s := 0.U
    val count_clk = Reg(UInt(25.W))
    when(count_clk === 24999999.U){
      count_clk := 0.U
      clk_1s := ~clk_1s
    }.otherwise{
      count_clk := count_clk + 1.U
    }

    val counter = Module(new Counter(10))
    counter.io.en := 1.U

    val num1 = Module(new bcd7seg())
    num1.io.en := 1.U
    num1.io.num := (counter.io.out % 10.U(4.W))(3, 0)
    io.seg0 := num1.io.HEX

    val num2 = Module(new bcd7seg())
    num2.io.en := 1.U
    num2.io.num := ((counter.io.out % 100.U)/10.U)(3, 0)
    io.seg1 := num2.io.HEX

    val num3 = Module(new bcd7seg())
    num3.io.en := 1.U
    num3.io.num := ((counter.io.out % 100.U)/10.U)(3, 0)
    io.seg2 := num3.io.HEX
  }


}
