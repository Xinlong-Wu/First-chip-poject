/***************************************************************************************
* Copyright (c) 2014-2022 Zihao Yu, Nanjing University
*
* NEMU is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
*
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
*
* See the Mulan PSL v2 for more details.
***************************************************************************************/

#include <isa.h>
#include "local-include/reg.h"

const char *regs[] = {
  "$0", "ra", "sp", "gp", "tp", "t0", "t1", "t2",
  "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
  "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
  "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
};

void isa_reg_display(char* reg) {
  char *cmd = NULL;
  if(reg != NULL)
    cmd = strtok(reg, " ");

  if (cmd == NULL){
    printf("Reg: ");
    for (int i = 0; i < 32; i++){
      printf("%s: %lu(0x%lx)\t\t",regs[i], cpu.gpr[i], cpu.gpr[i]);
      if(i % 4 == 0)
        printf("\n");
    }
    printf("\n");
    return;
  }

  bool isSuccess = false;
  word_t res = isa_reg_str2val(cmd, &isSuccess);

  if(isSuccess)
    printf("Reg %s: %lu (0x%lx)\n", cmd, res, (sword_t)res);
  else{
    printf("%s\n", ANSI_FMT(str(Error: wrong register.), ANSI_FG_RED));
    isa_reg_display(NULL);
  }

  return;
}

word_t isa_reg_str2val(const char *s, bool *success) {
  assert(s != NULL && success != NULL);

  *success = false;

  if(strcmp(s, "zero") == 0){
    *success = true;
    return cpu.gpr[0];
  }

  if(*s == 'x'){
    int index = atoi(s+1);
    if (index < 32){
      *success = true;
      return cpu.gpr[index];
    }
    return 0;
  }

  if (strcmp(s, "pc") == 0){
    *success = true;
    return cpu.pc;
  }
  
  for (int i = 0;i < 32; i++){
    if(strcmp(s, regs[i]) == 0){
      *success = true;
      return cpu.gpr[i];
    }
  }
  return 0;
}
