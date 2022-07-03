#ifndef __SDB_H__
#define __SDB_H__

#include"common.h"
#include"macro.h"

extern void init_sdb();

extern uint32_t inst_rom[];

extern void sdb_mainloop();
extern int cmd_help(char *args);
extern int cmd_c(char *args);
extern int cmd_q(char *args);
extern int cmd_si(char *args);

static struct {
  const char *name;
  const char *description;
  int (*handler) (char *);
} cmd_table [] = {
  { "help", "Display informations about all supported commands", cmd_help },
  { "c", "Continue the execution of the program", cmd_c },
  { "q", "Exit NEMU", cmd_q },

//   /* TODO: Add more commands */
  {"si", "si [N], step in the program", cmd_si},
//   {"info", "info [r|w], out put the info of Regesiter or WatchPoint", cmd_info},
//   {"x", "x [N] [EXPR] , out put N Bite data from value of EXPR by sixteen format", cmd_x},
//   {"p", "p [EXPR], out put the value of EXPR", cmd_p},
//   {"w", "w [EXPR], set WatchPoint stop the program if the value of EXPR has changed", cmd_w},
//   {"d", "d [N], delete WatchPoint witch id is N", cmd_d},
//   {"b", "b [EXPR], set breakoint at address of EXPR", cmd_b},
//   {"g", "an empty command for exit into gdb", cmd_gdb},
};

#define NR_CMD ARRLEN(cmd_table)

#endif
