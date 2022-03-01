import chisel3._
import chisel3.util.{Cat, Fill}
import chisel3.util.experimental.loadMemoryFromFile

class cmd_ctrl extends Module {
  val io = IO(new Bundle() {
    val h_addr = Input(UInt(10.W))  // 当前屏幕坐标
    val v_addr = Input(UInt(10.W))
    val data = Output(UInt(24.W))
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

  // 将屏幕上的坐标转换成buffer的坐标
  val index = Wire(UInt(10.W))
  index := (io.v_addr(9,4) * (h_display/w_ch).U(10.W)) + io.h_addr(9,3)

  val screen_buffer = Mem((h_display/w_ch * v_display/h_ch)+10, UInt(8.W))
  loadMemoryFromFile(screen_buffer, "/home/vincent/CodeSpace/First-chip-poject/npc/resource/screen_buffer.hex")

  val ch_index = Wire(UInt(8.W)) // 字符在字模中的起始位置
  ch_index := screen_buffer.read(index) - 32.U

  val temp = Wire(UInt(8.W))
  temp := screen_buffer.read(index)

  val templete = Mem(block_num, UInt(block_size.W))
  loadMemoryFromFile(templete, "/home/vincent/CodeSpace/First-chip-poject/npc/resource/AsciiMask.hex")

  var block_index = Wire(UInt(9.W)) // 计算该字符在字模内存的哪一个block里
  block_index := ch_index * 4.U /* 基block */ + io.v_addr(3,2) /* 该像素点偏移的block */

  var ch_data = Wire(UInt(block_size.W)) // 字符的像素点数据
  ch_data := templete.read(block_index)
  printf(p"index $index, ch_index $ch_index, block_index $block_index\n")

  var data_offset = Wire(UInt(8.W))
  data_offset := 31.U - ((io.v_addr(1,0) * 8.U) + io.h_addr(2,0))

//  printf(p"index $index, ch_index $ch_index, block_index $block_index, ch_data $ch_data, data_offset $data_offset, io:$io\n")

  io.data := Fill(24, ch_data(data_offset))
//  io.data := Fill(24, 1.U)
}
