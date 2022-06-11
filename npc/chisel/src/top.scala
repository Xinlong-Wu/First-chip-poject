import Decoder.{ALUOpType, FuType}
import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val io = IO(new Bundle() {
    val inst = (Input(UInt(32.W)))
    val pc_addr =(Output(UInt(width.W)))
    val pc_re =(Output(Bool()))
  })

  val gpr = Module(new GPR(width))
  gpr.io.clk := clock
  gpr.io.rst := reset

  val pc_reg = Module(new PC(width))
  io.pc_addr := pc_reg.io.pc_addr
  io.pc_re := pc_reg.io.pc_re

//  val if_id = Module(new IF_ID(width))
//  if_id.io.if_pc := pc_reg.io.pc_addr
//  if_id.io.if_inst := io.inst

//  val ifu = Module(new IFU(width))

  val idu = Module(new IDU(width))

  idu.io.inst_data := io.inst
  idu.io.pc_addr := pc_reg.io.pc_addr
  idu.io.reg1_data_i := gpr.io.rdata1
  idu.io.reg2_data_i := gpr.io.rdata2

  gpr.io.ren1 := idu.io.reg1_re
  gpr.io.ren2 := idu.io.reg2_re
  gpr.io.raddr1 := idu.io.reg1_rid
  gpr.io.raddr2 := idu.io.reg2_rid


  val exu = Module(new EXU(width))
  exu.io.fuop := idu.io.fuop
  exu.io.aluty := idu.io.aluty
  exu.io.reg1_re := idu.io.reg1_re
  exu.io.reg2_re := idu.io.reg2_re
  exu.io.imm_data := idu.io.imm_data

  exu.io.reg1_data := idu.io.reg1_data_o
  exu.io.reg2_data := idu.io.reg2_data_o

  val wbu = Module(new WBU(width))
  wbu.io.rf_wb := idu.io.rd_we
  wbu.io.wb_pc := idu.io.wb_pc
  wbu.io.rd_id := idu.io.rd_id
  wbu.io.wdata_i := exu.io.wdata
  wbu.io.pc_addr := pc_reg.io.pc_addr
  wbu.io.wb_pc := idu.io.wb_pc

  pc_reg.io.pc_we := wbu.io.pc_we
  pc_reg.io.pc_waddr := wbu.io.pc_waddr

  gpr.io.wen := wbu.io.rf_wb
  gpr.io.waddr := wbu.io.rd_id
  gpr.io.wdata := wbu.io.wdata_o

  val dpic = Module(new DPIC())
  dpic.io.is_ebreak := Mux(idu.io.fuop === FuType.ebreak, 1.U, 0.U)
  dpic.io.result := gpr.io.DPIC_res
  dpic.io.unimp := Mux(idu.io.aluty === ALUOpType.unimp, 1.U, 0.U)
}
