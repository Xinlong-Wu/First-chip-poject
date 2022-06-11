import chisel3._
import Decoder.{ImmFormat, RVDecoder, SrcType}
import chisel3.util.MuxCase

class IDU(width: Int) extends Module {
  val io = IO(new Bundle() {
    val pc_addr = Input(UInt(width.W))
    val inst_data = Input(UInt(32.W))
    val reg1_data_i = Input(UInt(width.W))
    val reg2_data_i = Input(UInt(width.W))

    val reg1_re = Output(Bool())
    val reg2_re = Output(Bool())
    val rd_we = Output(Bool())
    val rd_id = Output(UInt(5.W))
    val reg1_rid = Output(UInt(5.W))
    val reg2_rid = Output(UInt(5.W))
    val reg1_data_o = Output(UInt(width.W))
    val reg2_data_o = Output(UInt(width.W))
    val imm_data = Output(UInt(width.W))

    val fuop = Output(UInt(8.W))
    val aluty = Output(UInt(3.W))
    val wb_pc = Output(Bool())
  })

  val decoder = new RVDecoder(io.inst_data)

  val instInfo = decoder.getInstInfo()

  // rd reg
  io.rd_id := MuxCase(0.U, Array(
    (SrcType.reg === instInfo(0)) -> decoder.getRd(),
    reset.asBool -> 0.U
  ))

  io.rd_we := instInfo(5)

  // rs1
  io.reg1_re := MuxCase(0.U, Array(
    (SrcType.pc === instInfo(1)) -> true.B,
    (SrcType.reg === instInfo(1)) -> true.B,
    reset.asBool -> 0.U
  ))
  io.reg1_rid := MuxCase(0.U, Array(
    (SrcType.reg === instInfo(1)) -> decoder.getReg1(),
    reset.asBool -> 0.U
  ))
  io.reg1_data_o := Mux(SrcType.pc === instInfo(1), io.pc_addr, io.reg1_data_i)

  // rs2
  io.reg2_re := MuxCase(0.U, Array(
    (SrcType.reg === instInfo(2)) -> true.B,
    reset.asBool -> 0.U
  ))
  io.reg2_rid := MuxCase(0.U, Array(
    (SrcType.reg === instInfo(2)) -> decoder.getReg2(),
    reset.asBool -> 0.U
  ))
  io.reg2_data_o := io.reg2_data_i

  io.imm_data := MuxCase(0.U, Array(
    (ImmFormat.INST_U === instInfo(7)) -> decoder.getImmU(),
    (ImmFormat.INST_I === instInfo(7)) -> decoder.getImmI(),
    (ImmFormat.INST_J === instInfo(7)) -> decoder.getImmJ(),
    reset.asBool -> 0.U,
  ))
  io.fuop := Mux(reset.asBool, 0.U, instInfo(3))
  io.aluty := Mux(reset.asBool, 0.U, instInfo(4))

  io.wb_pc := instInfo(6)

  printf("[IDU] fuop: %b\n",instInfo(3))
  printf("[IDU] aluty: %b\n",instInfo(4))
  printf("[IDU] instruction: 0x%x\n",io.inst_data)

  printf("[IDU]: rd=0x%x, rs1=0x%x, rs2=0x%x\n",io.rd_id,io.reg1_rid,io.reg2_rid)
}
