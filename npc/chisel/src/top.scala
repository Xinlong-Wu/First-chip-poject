import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val io = IO(new Bundle() {
    val inst = (Input(UInt(32.W)))
    val pc =(Output(UInt(width.W)))
  })

  val pc_reg = Module(new PC(width))
  io.pc := pc_reg.io.pc_addr

  val if_id = Module(new IF_ID(width))
  if_id.io.if_pc := pc_reg.io.pc_addr
  if_id.io.if_inst := io.inst

//  val ifu = Module(new IFU(width))

  val idu = Module(new IDU(width))
//  idu.io.pc_addr := if_id.io.if_pc
//  idu.io.pc_addr := if_id.io.if_pc
}
