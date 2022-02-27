import Chisel.Cat
import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class led extends BlackBox{
  val io = Wire(new Bundle {
    val clk = Input(UInt(1.W))
    val rst = Input(UInt(1.W))
    val sw = Input(UInt(8.W))
    val ledr = Output(UInt(16.W))
  })
}

class light extends BlackBox{
  val io = IO(new Bundle {
    val clk = Input(Clock())
    val rst = Input(UInt(1.W))
    val led = Output(UInt(12.W))
  })
}

class vga_graph_ctrl extends BlackBox{
  val io = IO(new Bundle {
    val pclk = Input(UInt(1.W))
    val reset = Input(UInt(1.W))
    val vga_data = Input(UInt(24.W))
    val h_addr = Output(UInt(10.W))
    val v_addr = Output(UInt(10.W))
    val hsync = Output(UInt(1.W))
    val vsync = Output(UInt(1.W))
    val valid = Output(UInt(1.W))
    val vga_r = Output(UInt(8.W))
    val vga_g = Output(UInt(8.W))
    val vga_b = Output(UInt(8.W))
  })
}

class ps2_keyboard extends BlackBox{
  val io = IO(new Bundle {
    val clk = Input(UInt(1.W))
    val clrn = Input(UInt(1.W))
    val ps2_clk = Input(UInt(1.W))
    val ps2_data = Input(UInt(1.W))
    val nextdata_n = Input(UInt(1.W))
    val data = Output(UInt(8.W))
    val ready = Output(UInt(1.W))
    val overflow = Output(UInt(1.W))
  })
}

