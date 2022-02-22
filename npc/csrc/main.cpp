#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <verilated.h>          
#include <verilated_vcd_c.h>
#include "TEMP.h"

static VGCD* top;
static VerilatedVcdC* tfp;
static vluint64_t main_time = 0;
static const vluint64_t sim_time = 1000;

int main(int argc, char **argv) {
  // initialization
  Verilated::commandArgs(argc, argv);
  Verilated::traceEverOn(true);

	top = new VGCD;
  tfp = new VerilatedVcdC;

  top->trace(tfp, 99);
  tfp->open("/home/vincent/CodeSpace/First-chip-poject/build/top.vcd");
  while (!Verilated::gotFinish() && main_time < sim_time) {
    if( main_time % 10 == 0 ) top->clock = 0;
	  if( main_time % 10 == 5 ) top->clock = 1;

    // int a = rand() & 1;
    // int b = rand() & 1;
    // top->a = a;
    // top->b = b;
    
    top->eval();
    tfp->dump(main_time);
	  main_time++;
    // assert(top->f == a ^ b);
  }
  printf("/home/vincent/CodeSpace/First-chip-poject/build/top.vcd");

  // clean
  tfp->close();
  delete top;
  delete tfp;
  exit(0);
  return 0;
}