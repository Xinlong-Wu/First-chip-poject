#ifndef __VERILATOR_H__
#define __VERILATOR_H__

#include <Vtop.h>
#include <verilated.h>          
#include <verilated_vcd_c.h>

static TOP_NAME* top;
static VerilatedVcdC* tfp;
static vluint64_t main_time = 0;
static const vluint64_t sim_time = 1000;

static std::string wave_file = "/home/vincent/CodeSpace/First-chip-poject/npc/build/top.vcd";


extern void init_verilator(int argc, char *argv[]);

extern void reset(int n);
extern void single_cycle();
extern int npc_exec(int n);

#endif