import chisel3._
import chisel3.util.Counter

class clkgen(clk_freq: Int = 1000) extends Module {
  val io = IO(new Bundle() {
    val clken = Input(Bool())
    val clkout = Input(Reg(Bool()))
  })

  val countlimit = 50000000/2/clk_freq

  val (_, clk_1s) = Counter(io.clken, countlimit)

  when(clk_1s){
    io.clkout := ~io.clkout
  }
}
