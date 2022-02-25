import chisel3._
import chisel3.util.HasBlackBoxInline

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
    val clk = Input(UInt(1.W))
    val rst = Input(UInt(1.W))
    val led = Output(UInt(12.W))
  })
}

class vga_ctrl extends BlackBox{
  val io = IO(new Bundle {
    val pclk = Input(UInt(1.W))
    val reset = Input(UInt(1.W))
    val vga_data = Output(UInt(24.W))
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
    val resetn = Input(UInt(1.W))
    val ps2_clk = Input(UInt(1.W))
    val ps2_data = Input(UInt(1.W))
  })
}

class vmem extends BlackBox with HasBlackBoxInline{
  val io = IO(new Bundle {
    val h_addr = Input(UInt(10.W))
    val v_addr = Input(UInt(9.W))
    val vga_data = Output(UInt(24.W))
  })

  setInline("vmem.v",
    """
      |module vmem (
      |    input [9:0] h_addr,
      |    input [8:0] v_addr,
      |    output [23:0] vga_data
      |);
      |
      |reg [23:0] vga_mem [524287:0];
      |
      |initial begin
      |    $readmemh("npc/resource/picture.hex", vga_mem);
      |end
      |
      |assign vga_data = vga_mem[{h_addr, v_addr}];
      |
      |endmodule
    """.stripMargin)
}
