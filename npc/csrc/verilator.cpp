#include"include/verilator.h"
#include"include/common.h"
#include"include/DPIC.h"

#include<cstdio>

void init_verilator(int argc, char *argv[]){
  // Verilated::commandArgs(int argc, char *argv[]);
  Verilated::traceEverOn(true);
  top = new TOP_NAME;
  tfp = new VerilatedVcdC;

  reset(10);

  top->trace(tfp, 99);
  tfp->open(wave_file.c_str());
}

void reset(int n){
  top->reset = 1;
  while (n -- > 0) {
    top->clock = 0; top->eval();
    top->clock = 1; top->eval();
  }
  top->reset = 0;
}

extern uint32_t inst_rom[];
int npc_exec(int n){
  int counter = 0;
  for (int i = 0; (n < 0 || i < n) && !is_ebreak; i++){
    top->clock = !top->clock;

    if((top->io_imem_addr%0x80000000)>>2 >= 65536){
      printf("[Error]: %x End of Rom",top->io_imem_addr);
      exit(-1);
    }

    // top->io_inst = (top->io_pc_re == 1) ? inst_rom[ (top->io_pc_addr%0x80000000) >> 2 ] : 0;
    top->io_imem_inst = inst_rom[ (top->io_imem_addr%0x80000000) >> 2 ];
    top->eval();
    tfp->dump(main_time);

    if(counter%2 == 0){
      printf("\n");
    }
    counter++;
    main_time++;
  }

  return 0;
}

void single_cycle() {
  npc_exec(2);
}