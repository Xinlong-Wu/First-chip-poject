// #include <nvboard.h>
#include <Vtop.h>
#include <verilated.h>          
#include <verilated_vcd_c.h>

static TOP_NAME* top;
static VerilatedVcdC* tfp;
static vluint64_t main_time = 0;
static const vluint64_t sim_time = 1000;

// void nvboard_bind_all_pins(Vtop* top);

static void single_cycle() {
  top->clock = 0; top->eval();
  top->clock = 1; top->eval();
}

static void reset(int n) {
  top->reset = 1;
  while (n -- > 0) single_cycle();
  top->reset = 0;
}

int main(int argc, char **argv) {
  // initialization
  Verilated::commandArgs(argc, argv);
  Verilated::traceEverOn(true);
	top = new TOP_NAME;
  tfp = new VerilatedVcdC;
  // nvboard_bind_all_pins(&top);
  // nvboard_init();
  reset(10);

  top->trace(tfp, 99);
  tfp->open("./build/top.vcd");
  while(!Verilated::gotFinish() && main_time < sim_time) {
    // nvboard_update();
    top->clock = !top->clock;
    top->eval();
    tfp->dump(main_time);
    main_time++;
  }

  tfp->close();
  delete top;
  delete tfp;
  exit(0);
  return 0;
}
