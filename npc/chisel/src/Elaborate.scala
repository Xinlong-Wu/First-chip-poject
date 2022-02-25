object Elaborate extends App {
  var modules = Array(new mux24(), new bcd7seg(), new encoder83())
  for(obj <- modules){
    (new chisel3.stage.ChiselStage).execute(args, Seq(chisel3.stage.ChiselGeneratorAnnotation(() => obj)))
  }
}
