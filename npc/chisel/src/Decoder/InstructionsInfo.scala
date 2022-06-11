package Decoder

import Decoder.Instructions._
import chisel3._
import chisel3.util.{BitPat, log2Up}

object SrcType {
  def reg = "b00".U
  def pc  = "b01".U
  def imm = "b01".U

  def unknow = imm

  def isReg(srcType: UInt) = srcType===reg
  def isPc(srcType: UInt) = srcType===pc
  def isImm(srcType: UInt) = srcType===imm
  def isPcOrImm(srcType: UInt) = srcType(0)

  def apply() = UInt(2.W)
}

object FuType {
  def alu = "b0001".U
  def jmp = "b0010".U
  def ebreak = "b1111".U

  def typeCount = 2

  def apply() = UInt(log2Up(typeCount).W)
}

object ALUOpType{
  def addi = "b0000_0001".U

  def apply() = UInt(8.W)
}

object JumpOpType{
  def jal = "b0000_0001".U

  def apply() = UInt(8.W)
}

object InstType{
  def INVALID_INST = "b0000_0000".U
  def addi = "b0000_0001".U

  def apply() = UInt(8.W)
}

object wbType{
  def INVALID_INST = "b0000_0000".U
  def addi = "b0000_0001".U

  def apply() = UInt(8.W)
}

object ImmFormat {
  def INST_I  = "b0001".U
  def INST_U = "b0010".U
  def INST_J = "b0011".U

  def INVALID = "b1111".U

  def apply() = UInt(4.W)
}

abstract trait InstructionsInfo {
  def N = false.B
  def Y = true.B

  def decodeDefault: List[UInt] = // illegal instruction
  //      srcType(0)      srcType(1)      srcType(2)      fuType          aluType    rfWe wbPC ImmFormat
  //        |               |               |               |               |         |   |    |
     List(SrcType.unknow, SrcType.unknow, SrcType.unknow, FuType.alu, ALUOpType.addi, N, N, ImmFormat.INVALID)

//  def decodeDefault = InstType.INVALID_INST

  val table: Array[(BitPat, List[UInt])]
//  val table: Array[(BitPat, UInt)]
}

object RVInstInfo extends InstructionsInfo{
  val table: Array[(BitPat, List[UInt])] = Array(
    ADDI -> List(SrcType.reg, SrcType.reg, SrcType.imm, FuType.alu, ALUOpType.addi, Y, N, ImmFormat.INST_I),

    AUIPC -> List(SrcType.reg, SrcType.pc, SrcType.imm, FuType.alu, ALUOpType.addi, Y, N, ImmFormat.INST_U),

    JAL -> List(SrcType.reg, SrcType.pc, SrcType.imm, FuType.jmp, JumpOpType.jal, Y, Y, ImmFormat.INST_J),

    EBREAK -> List(SrcType.unknow, SrcType.unknow, SrcType.unknow, FuType.ebreak, ALUOpType.addi, N, N, ImmFormat.INVALID)
  )
//  val table: Array[(BitPat, UInt)] = Array(
//    ADDI -> InstType.addi
//  )
}
