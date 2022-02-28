import chisel3._

class vga_ctrl extends Module {
  val io = IO(new Bundle {
    val vga_data = Input(UInt(24.W))  //上层模块提供的VGA颜色数据
    val h_addr = Output(UInt(10.W))   //提供给上层模块的当前扫描像素点坐标
    val v_addr = Output(UInt(10.W))
    val hsync = Output(UInt(1.W))     //行同步和列同步信号
    val vsync = Output(UInt(1.W))
    val valid = Output(UInt(1.W))     //消隐信号
    val vga_r = Output(UInt(8.W))     //红绿蓝颜色信号
    val vga_g = Output(UInt(8.W))
    val vga_b = Output(UInt(8.W))
  })

  //640x480分辨率下的VGA参数设置
  val h_frontporch = 96
  val h_active = 144
  val h_backporch = 784
  val h_total = 800

  val v_frontporch = 2
  val v_active = 35
  val v_backporch = 515
  val v_total = 525

  //像素计数值
  val x_cnt = Reg(UInt(10.W))
  val y_cnt = Reg(UInt(10.W))
  val h_valid = Wire(Bool())
  val v_valid = Wire(Bool())

  when(reset.asBool){
    x_cnt := 1.U(10.W)
    y_cnt := 1.U(10.W)
  }

  when(y_cnt === v_total.U && x_cnt === h_total.U){ //列像素计数
    y_cnt := 1.U(10.W)
  }.elsewhen(x_cnt === h_total.U){
    y_cnt := y_cnt + 1.U(10.W)
  }

  when(x_cnt === h_total.U){  //行像素计数
    x_cnt := 1.U(10.W)
  }.otherwise{
    x_cnt := x_cnt + 1.U(10.W)
  }

  //生成同步信号
  io.hsync := (x_cnt > h_frontporch.U)
  io.vsync := (y_cnt > v_frontporch.U)
  //生成消隐信号
  h_valid := (x_cnt > h_active.U) & (x_cnt <= h_backporch.U)
  v_valid := (y_cnt > v_active.U) & (y_cnt <= v_backporch.U)
  io.valid := h_valid & v_valid
  //计算当前有效像素坐标
  io.h_addr := Mux(h_valid, (x_cnt - (h_active+1).U(10.W)), 0.U(10.W))
  io.v_addr := Mux(v_valid, (y_cnt - (v_active+1).U(10.W)), 0.U(10.W))
  //设置输出的颜色值
  io.vga_r := io.vga_data(23,16)
  io.vga_g := io.vga_data(15,8)
  io.vga_b := io.vga_data(7,0)
}
