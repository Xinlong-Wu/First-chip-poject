import chisel3._

class encoder83 extends Module{
  val io = IO(new Bundle() {
    val x = Input(UInt(8.W))
    val en = Input(Bool())
    val y = Output(UInt(3.W))
  })
  io.y := 0.U
  when(io.en){
    for (i <- 0 until 8){
      when(io.x(i)===1.U){
        io.y := i.U
      }
    }
  }
}
