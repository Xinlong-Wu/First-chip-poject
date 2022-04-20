// #include <nvboard.h>
#include <iostream>
#include <Vtop.h>
#include <verilated.h>          
#include <verilated_vcd_c.h>
#include <Vtop__Dpi.h>

static TOP_NAME* top;
static VerilatedVcdC* tfp;
static vluint64_t main_time = 0;
static const vluint64_t sim_time = 1000;

// void nvboard_bind_all_pins(Vtop* top);

bool is_ebreak = false;
void hit_ebreak(){
  std::cout<<"hit ebreak"<<std::endl;
  is_ebreak = true;
}

static void single_cycle() {
  top->clock = 0; top->eval();
  top->clock = 1; top->eval();
}

static void reset(int n) {
  top->reset = 1;
  while (n -- > 0) single_cycle();
  top->reset = 0;
}

int inst_rom[65536];

void read_inst( char* filename)
{
  FILE *fp = fopen(filename, "rb");
  if( fp == NULL ) {
		printf( "Can not open this file!\n" );
		exit(1);
  }
  
  fseek(fp, 0, SEEK_END);
  size_t size = ftell(fp);
  fseek(fp, 0, SEEK_SET);
  size = fread(inst_rom, size, 1, fp);
  fclose(fp);
}

int main(int argc, char **argv) {
  if (argc == 1){
    std::cout<<"no elf file was specify"<<std::endl;
    exit(0);
  }
  
  read_inst(argv[1]);

  // initialization
  Verilated::commandArgs(argc-2, argv+2);
  Verilated::traceEverOn(true);
	top = new TOP_NAME;
  tfp = new VerilatedVcdC;
  // nvboard_bind_all_pins(&top);
  // nvboard_init();
  reset(10);

  top->trace(tfp, 99);
  tfp->open("./build/top.vcd");
  while(!is_ebreak) {
    // nvboard_update();
    top->clock = !top->clock;
    top->io_inst = (top->io_pc_re == 1) ? inst_rom[ (top->io_pc_addr%0x80000000) >> 2 ] : 0;
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
