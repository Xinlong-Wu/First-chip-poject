import Decoder.{ALUOpType, FuType}
import chisel3._

class Core(width: Int) extends Module {
  val io = IO(new Bundle() {
    val imem = Flipped(new ImemPortIo(width))
    val dmem = Flipped(new DmemPortIo(width))

    // for debug
    val instInfo = new InstInfo()
    val DPIC_res = Output(UInt(width.W))
  })

  val gpr = Module(new GPR(width))
  gpr.io.clk := clock
  gpr.io.rst := reset

  val pc_reg = Module(new PC(width))
  io.imem.addr := pc_reg.io.pc_addr

  //  val if_id = Module(new IF_ID(width))
  //  if_id.io.if_pc := pc_reg.io.pc_addr
  //  if_id.io.if_inst := io.inst

  //  val ifu = Module(new IFU(width))

  val idu = Module(new IDU(width))

  idu.io.inst_data := io.imem.inst
  idu.io.pc_addr := pc_reg.io.pc_addr
  idu.io.reg1_data_i := gpr.io.rdata1
  idu.io.reg2_data_i := gpr.io.rdata2

  gpr.io.ren1 := idu.io.reg1_re
  gpr.io.ren2 := idu.io.reg2_re
  gpr.io.raddr1 := idu.io.reg1_rid
  gpr.io.raddr2 := idu.io.reg2_rid


  val exu = Module(new EXU(width))
  exu.io.inst_info <> idu.io.inst_info
  exu.io.reg1_re := idu.io.reg1_re
  exu.io.reg2_re := idu.io.reg2_re
  exu.io.imm_data := idu.io.imm_data

  exu.io.reg1_data := idu.io.reg1_data_o
  exu.io.reg2_data := idu.io.reg2_data_o

  val wbu = Module(new WBU(width))
  wbu.io.inst_info <> idu.io.inst_info
  wbu.io.rd_id := idu.io.rd_id
  wbu.io.wdata_i := exu.io.data_o
  wbu.io.pc_addr := pc_reg.io.pc_addr

  io.dmem.wen := wbu.io.dmem_wen
  io.dmem.wdata := wbu.io.wdata_o
  io.dmem.addr := exu.io.imm_data

  pc_reg.io.pc_we := wbu.io.pc_we
  pc_reg.io.pc_waddr := wbu.io.pc_waddr

  gpr.io.wen := wbu.io.we
  gpr.io.waddr := wbu.io.wid
  gpr.io.wdata := wbu.io.wdata_o

  // for test
  io.instInfo <> idu.io.inst_info
  io.DPIC_res := gpr.io.DPIC_res
}
