import chisel3.fromIntToLiteral
import chiseltest.ChiselUtestTester

object mux24Spac extends ChiselUtestTester{
   "test MUX24" in {
     test(new mux24){
       print("Mux test")
     }
   }
}
