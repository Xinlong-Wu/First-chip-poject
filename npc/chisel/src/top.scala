import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val inst = IO(Input(UInt(32.W)))
  val pc =IO(Output(UInt(width.W)))

  val pc_reg = Module(new PC(width))
  pc := pc_reg.io.pc_addr

  val gpr = Module(new GPR(width))
  gpr.io.id := DontCare
  gpr.io.wenable := DontCare
  gpr.io.wdata := DontCare
  gpr.io.rdata := DontCare

//  val ifu = Module(new IFU(width))

}
