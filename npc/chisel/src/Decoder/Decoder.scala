package Decoder

import chisel3._
import chisel3.util._

class RVDecoder(inst_data: UInt) {

  def inst = inst_data

  def getInstInfo(): List[UInt] = {
    ListLookup(inst_data, RVInstInfo.decodeDefault, RVInstInfo.table)
  }

  def getRd(): UInt = inst(11,7)

  def getReg1(): UInt = inst(19,15)

  def getReg2(): UInt = inst(24,20)

  def getImmI(): UInt = {
    val imm = inst(31,20)
    Cat(Fill(20,imm(11)),imm)
  }
}
