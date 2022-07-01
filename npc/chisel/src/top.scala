import Decoder.{ALUOpType, FuType}
import chisel3._
import chisel3.util.Counter

class top(width: Int) extends Module{
  val io = IO(new Bundle() {
    val imem = Flipped(new ImemPortIo(width))
  })

  val core = Module(new Core(width))
  io.imem <> core.io.imem

  val memory = Module(new Memory(width))
  core.io.dmem <> memory.io.dmem

  val dpic = Module(new DPIC())
  dpic.io.is_ebreak := Mux(core.io.instInfo.fuop === FuType.ebreak, 1.U, 0.U)
  dpic.io.result := core.io.DPIC_res
  dpic.io.unimp := Mux(core.io.instInfo.aluty === ALUOpType.unimp, 1.U, 0.U)
}
