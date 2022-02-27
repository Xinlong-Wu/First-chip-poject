import Chisel.Cat
import chisel3.util.experimental.loadMemoryFromFile
import chisel3._

class vmem(block_num:Int, block_size:Int, file_path:String) extends Module{
  val io = IO(new Bundle {
    val h_addr = Input(UInt(10.W))
    val v_addr = Input(UInt(9.W))
    val vga_data = Output(UInt(block_size.W))
  })
  val mem = Mem(block_num, UInt(block_size.W))
  loadMemoryFromFile(mem, file_path)

  io.vga_data := mem.read(Cat(io.h_addr, io.v_addr))
}