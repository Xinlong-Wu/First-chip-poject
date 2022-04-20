import Decoder.FuType
import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val io = IO(new Bundle() {
    val inst = (Input(UInt(32.W)))
    val pc_addr =(Output(UInt(width.W)))
    val pc_re =(Output(Bool()))
  })

  val gpr = Module(new GPR(width))

  val pc_reg = Module(new PC(width))
  io.pc_addr := pc_reg.io.pc_addr
  io.pc_re := pc_reg.io.pc_re

//  val if_id = Module(new IF_ID(width))
//  if_id.io.if_pc := pc_reg.io.pc_addr
//  if_id.io.if_inst := io.inst

//  val ifu = Module(new IFU(width))

  val idu = Module(new IDU(width))
  idu.io.inst_data := io.inst
  gpr.io.re1 := idu.io.reg1_re
  gpr.io.re2 := idu.io.reg2_re
  gpr.io.rid1 := idu.io.reg1_rid
  gpr.io.rid2 := idu.io.reg2_rid

  val exu = Module(new EXU(width))
  exu.io.fuop := idu.io.fuop
  exu.io.aluty := idu.io.aluty
  exu.io.reg1_re := idu.io.reg1_re
  exu.io.reg2_re := idu.io.reg2_re
  exu.io.rd_we := idu.io.rd_we
  exu.io.rd_id := idu.io.rd_id
  exu.io.imm_data := idu.io.imm_data

  exu.io.reg1_data := gpr.io.rdata1
  exu.io.reg2_data := gpr.io.rdata2

  gpr.io.we := exu.io.wrd_en
  gpr.io.wid := exu.io.wrd_id
  gpr.io.wdata := exu.io.wdata

  val dpic = Module(new DPIC())
  dpic.io.is_ebreak := Mux(idu.io.fuop === FuType.ebreak, 1.U, 0.U)
}
