import chisel3._
import chisel3.util.{is, switch}

class scancode2ascii extends Module {
  val io = IO(new Bundle() {
    val scan_code = Input(UInt(8.W))
    val ascii = Output(UInt(8.W))
  })

  io.ascii := 0.U(8.W)

  switch(io.scan_code){
    is("h16".U){io.ascii := "h31".U(8.W)} // 1
    is("h1E".U){io.ascii := "h32".U(8.W)} // 2
    is("h26".U){io.ascii := "h33".U(8.W)} // 3
    is("h25".U){io.ascii := "h34".U(8.W)} // 4
    is("h2E".U){io.ascii := "h35".U(8.W)} // 5
    is("h36".U){io.ascii := "h36".U(8.W)} // 6
    is("h3D".U){io.ascii := "h37".U(8.W)} // 7
    is("h3E".U){io.ascii := "h38".U(8.W)} // 8
    is("h46".U){io.ascii := "h39".U(8.W)} // 9
    is("h45".U){io.ascii := "h30".U(8.W)} // 0
    is("h4E".U){io.ascii := "h2D".U(8.W)} // -
    is("h55".U){io.ascii := "h3D".U(8.W)} // =
    is("h5D".U){io.ascii := "h5C".U(8.W)} // q
    is("h15".U){io.ascii := "h71".U(8.W)} // w
    is("h1D".U){io.ascii := "h77".U(8.W)} // e
    is("h24".U){io.ascii := "h65".U(8.W)} // r
    is("h2D".U){io.ascii := "h72".U(8.W)} // t
    is("h2C".U){io.ascii := "h74".U(8.W)} // y
    is("h35".U){io.ascii := "h79".U(8.W)} // u
    is("h3C".U){io.ascii := "h75".U(8.W)} // i
    is("h43".U){io.ascii := "h69".U(8.W)} // o
    is("h44".U){io.ascii := "h6F".U(8.W)} // p
    is("h4D".U){io.ascii := "h70".U(8.W)} // [
    is("h54".U){io.ascii := "h5B".U(8.W)} // ]
    is("h5B".U){io.ascii := "h5D".U(8.W)} // a
    is("h1C".U){io.ascii := "h61".U(8.W)} // s
    is("h1B".U){io.ascii := "h73".U(8.W)} // d
    is("h23".U){io.ascii := "h64".U(8.W)} // f
    is("h2B".U){io.ascii := "h66".U(8.W)} // g
    is("h34".U){io.ascii := "h67".U(8.W)} // h
    is("h33".U){io.ascii := "h68".U(8.W)} // j
    is("h3B".U){io.ascii := "h6A".U(8.W)} // k
    is("h42".U){io.ascii := "h6B".U(8.W)} // l
    is("h4B".U){io.ascii := "h6C".U(8.W)} // ;
    is("h4C".U){io.ascii := "h3B".U(8.W)} // '
    is("h52".U){io.ascii := "h27".U(8.W)} // z
    is("h1A".U){io.ascii := "h7A".U(8.W)} // x
    is("h22".U){io.ascii := "h78".U(8.W)} // c
    is("h21".U){io.ascii := "h63".U(8.W)} // v
    is("h2A".U){io.ascii := "h76".U(8.W)} // b
    is("h32".U){io.ascii := "h62".U(8.W)} // n
    is("h31".U){io.ascii := "h6E".U(8.W)} // n
    is("h3A".U){io.ascii := "h6D".U(8.W)} // m
    is("h41".U){io.ascii := "h2C".U(8.W)} // ,
    is("h49".U){io.ascii := "h2E".U(8.W)} // .
    is("h4A".U){io.ascii := "h2F".U(8.W)} // /
//    is("h5D".U){io.ascii := "h5C".U(8.W)}
//    is("h5D".U){io.ascii := "h5C".U(8.W)}


  }
}
