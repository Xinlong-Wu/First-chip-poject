#include <nvboard.h>
#include <Vtop.h>

static TOP_NAME top;

void nvboard_bind_all_pins(Vtop* top);

static void single_cycle() {
  top.io_clock = 0; top.eval();
  top.io_clock = 1; top.eval();
}

static void reset(int n) {
  top.io_reset = 1;
  while (n -- > 0) single_cycle();
  top.io_reset = 0;
}

int main() {
  nvboard_bind_all_pins(&top);
  nvboard_init();

  reset(10);

  while(1) {
    nvboard_update();
    top.io_clock = !top.io_clock;
    top.eval();
  }
}
