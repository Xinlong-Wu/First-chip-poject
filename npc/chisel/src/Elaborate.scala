object Elaborate extends App {

  var myarg = Array("-td", "/home/vincent/CodeSpace/First-chip-poject/npc/build")
  if(args.length != 0){
    myarg = args
  }
//  (new chisel3.stage.ChiselStage).execute(myarg, Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new bcd7seg())))
//  (new chisel3.stage.ChiselStage).execute(myarg, Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new LoadMem())))
  (new chisel3.stage.ChiselStage).execute(myarg, Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new chiseltop())))
}
