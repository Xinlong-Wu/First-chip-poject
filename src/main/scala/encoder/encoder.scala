package encoder

import chisel3._

class encoder extends Module{
  val io = IO(new Bundle() {
    val En = Input(Bool())
    val x = Input(UInt(4.W))

    val y = Output(UInt(2.W))
  })

  io.y := 0.U

  when(io.En){
    for(i <- 0 to 3){
      when(io.x(i)){
        io.y := i.U
      }
    }
  }

}
