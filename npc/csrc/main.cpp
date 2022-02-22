#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <verilated.h>          
#include <verilated_vcd_c.h>
#include <nvboard.h>
#include "TEMP.h"

static TOP_NAME dut;

void nvboard_bind_all_pins(Vtop* top);

static void single_cycle() {
  dut.clk = 0; dut.eval();
  dut.clk = 1; dut.eval();
}

static void reset(int n) {
  dut.rst = 1;
  while (n -- > 0) single_cycle();
  dut.rst = 0;
}

int main() {
  nvboard_bind_all_pins(&dut);
  nvboard_init();

  reset(10);

  while(1) {
    nvboard_update();
    dut.clk = !dut.clk;
    dut.eval();
  }
}

// static VGCD* top;
// static VerilatedVcdC* tfp;
// static vluint64_t main_time = 0;
// static const vluint64_t sim_time = 1000;

// int main(int argc, char **argv) {
//   // initialization
//   Verilated::commandArgs(argc, argv);
//   Verilated::traceEverOn(true);

// 	top = new VGCD;
//   tfp = new VerilatedVcdC;

//   top->trace(tfp, 99);
//   tfp->open(WAVE_FILE);
//   while (!Verilated::gotFinish() && main_time < sim_time) {
//     if( main_time % 10 == 0 ) top->clock = 0;
// 	  if( main_time % 10 == 5 ) top->clock = 1;

//     // int a = rand() & 1;
//     // int b = rand() & 1;
//     // top->a = a;
//     // top->b = b;
    
//     top->eval();
//     tfp->dump(main_time);
// 	  main_time++;
//     // assert(top->f == a ^ b);
//   }
//   printf(WAVE_FILE);

//   // clean
//   tfp->close();
//   delete top;
//   delete tfp;
//   exit(0);
//   return 0;
// }