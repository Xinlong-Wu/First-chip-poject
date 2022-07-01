import chisel3._
import chisel3.util.Cat

class ImemPortIo(width:Int) extends Bundle {
  val addr = Input(UInt(width.W))
  val inst = Output(UInt(width.W))
}

class DmemPortIo(width:Int) extends Bundle {
  val addr  = Input(UInt(width.W))
  val rdata = Output(UInt(width.W))
  val wen   = Input(Bool())
  val wdata = Input(UInt(width.W))
}

class Memory(width:Int) extends Module {
  val io = IO(new Bundle() {
//    val imem = new ImemPortIo(width)
    val dmem = new DmemPortIo(width)
  })

  val mem = Mem(16384, UInt(8.W))
  io.dmem.rdata := Cat(
    mem(io.dmem.addr + 7.U(width.W)),
    mem(io.dmem.addr + 6.U(width.W)),
    mem(io.dmem.addr + 5.U(width.W)),
    mem(io.dmem.addr + 4.U(width.W)),
    mem(io.dmem.addr + 3.U(width.W)),
    mem(io.dmem.addr + 2.U(width.W)),
    mem(io.dmem.addr + 1.U(width.W)),
    mem(io.dmem.addr)
  )

  mem(io.dmem.addr) := Mux(io.dmem.wen, io.dmem.wdata( 7, 0), mem(io.dmem.addr))
  mem(io.dmem.addr + 1.U) := Mux(io.dmem.wen, io.dmem.wdata(15, 8), mem(io.dmem.addr))
  mem(io.dmem.addr + 2.U) := Mux(io.dmem.wen, io.dmem.wdata(23,16), mem(io.dmem.addr))
  mem(io.dmem.addr + 3.U) := Mux(io.dmem.wen, io.dmem.wdata(31,24), mem(io.dmem.addr))
  mem(io.dmem.addr + 4.U) := Mux(io.dmem.wen, io.dmem.wdata(39,32), mem(io.dmem.addr))
  mem(io.dmem.addr + 5.U) := Mux(io.dmem.wen, io.dmem.wdata(47, 40), mem(io.dmem.addr))
  mem(io.dmem.addr + 6.U) := Mux(io.dmem.wen, io.dmem.wdata(55,48), mem(io.dmem.addr))
  mem(io.dmem.addr + 7.U) := Mux(io.dmem.wen, io.dmem.wdata(63,56), mem(io.dmem.addr))
}
