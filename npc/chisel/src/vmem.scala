import Chisel.Cat
import chisel3.util.experimental.loadMemoryFromFile
import chisel3._

class vmem extends Module{
  val io = IO(new Bundle {
    val h_addr = Input(UInt(10.W))
    val v_addr = Input(UInt(9.W))
    val vga_data = Output(UInt(24.W))
  })
  val mem = Mem(524288, UInt(24.W))
  loadMemoryFromFile(mem, "npc/resource/picture.hex")

  io.vga_data := mem.read(Cat(io.h_addr, io.v_addr))
}