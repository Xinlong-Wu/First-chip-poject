import Chisel.Cat
import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class screen_buffer extends Module {
  val io = IO(new Bundle() {
    val buffer_index = Input(UInt(12.W))
    val buffer_data = Output(UInt(8.W))
  })

  // 单个字符大小
  val h_ch = 16
  val w_ch = 8

  // 显示分辨率
  val h_display = 640
  val v_display = 480

  // 每一个block为32位宽，4个block为一个字符
  val block_size = 32
  // 总共380个block
  val block_num = 380

  val mem = Mem((h_display/w_ch * v_display/h_ch)+10, UInt(8.W))
  loadMemoryFromFile(mem, "/home/vincent/CodeSpace/First-chip-poject/npc/resource/screen_buffer.hex")

  io.buffer_data := mem.read(io.buffer_index)
}
