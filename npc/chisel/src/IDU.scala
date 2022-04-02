import chisel3._

class IDU(width: Int) extends Module {
  val io = IO(new Bundle() {

  })

  val gpr = Module(new GPR(width))


}
