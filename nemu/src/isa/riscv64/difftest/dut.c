#include <isa.h>
#include <cpu/difftest.h>
#include "../local-include/reg.h"

bool isa_difftest_checkregs(CPU_state *ref_r, vaddr_t pc) {
  bool res = (ref_r->pc == pc);
  if(res == false)
    log_write("ref pc = %lx, dut pc = %lx",ref_r->pc, pc);
  for (size_t i = 0; res == true && i < 32; i++){
    res = (gpr(i) == ref_r->gpr[i]);
    if(res == false)
      log_write("ref %s = %lx, dut %s = %lx",reg_name(i),ref_r->pc, reg_name(i), pc);
  }
  return res;
}

void isa_difftest_attach() {
}
