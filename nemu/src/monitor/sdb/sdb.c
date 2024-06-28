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
#include <cpu/cpu.h>
#include <readline/readline.h>
#include <readline/history.h>
#include <memory/vaddr.h>
#include <memory/paddr.h>
#include "sdb.h"
#include "watchpoint.h"

static int is_batch_mode = false;

void init_regex();
void init_wp_pool();

static int cmd_help(char *args);

/* We use the `readline' library to provide more flexibility to read from stdin. */
static char* rl_gets() {
  static char *line_read = NULL;

  if (line_read) {
    free(line_read);
    line_read = NULL;
  }

  line_read = readline("(nemu) ");

  if (line_read && *line_read) {
    add_history(line_read);
  }

  return line_read;
}

static int cmd_c(char *args) {
  cpu_exec(-1);
  return 0;
}


static int cmd_q(char *args) {
  return -1;
}

static int cmd_si(char *args){
  int n = 1;
  if(args)
    sscanf(args,"%d",&n);

  // printf("run %d time\n", n);
  
  struct Decode s;
  while (n--){
    exec_once(&s, cpu.pc);
    printf("%s\n", s.logbuf);
    trace_and_difftest(&s, cpu.pc);
    // printf("nemu_state.state %d\n", nemu_state.state);
    if (nemu_state.state != NEMU_STOP) break;
    IFDEF(CONFIG_DEVICE, device_update());
  }
  return 0;
}

static int cmd_info(char *args) {
  // Log("cmd_info get arg %s", args);
  char *cmd = strtok(args, " ");
  if (cmd == NULL){
    printf("%s\n", ANSI_FMT(str(Error: valid params.), ANSI_FG_RED));
    cmd_help(NULL);
    return 0;
  }
  
  char * expargs = strtok(NULL, " ");
  // printf("cmd %s, expargs %s\n", cmd, expargs);

  if (strcmp(cmd,"r") == 0){
    isa_reg_display(expargs);
  }
  else if (strcmp(cmd, "w") == 0){
    WP *p = get_wp_list();

    while (p != NULL){
      printf("watch pint N.%d: expr=%s, current value=%lu\n", p->NO, p->expr, p->expr_value);
      p = p->next;
    }
  }
  else{
    printf("%s\n", ANSI_FMT(str(Error: valid params.), ANSI_FG_RED));
    cmd_help(NULL);
  }

  return 0;
}

static int cmd_x(char *args) {

  int len = 0;
  paddr_t addr = 0;
  if(args){
    char * cmd = strtok(args, " ");
    len = atoi(cmd);
    char *exprstr = strtok(NULL, " ");

    bool isSuccess = false;
    if(exprstr!=NULL)
      addr = (paddr_t)expr(exprstr, &isSuccess);
    if(isSuccess){
      printf("expr %s, value is %u\n",args,addr);
    }
    else{
      printf("%s\n", ANSI_FMT(str(Error: Valit expr), ANSI_FG_RED));
      return 0;
    }
  }

  if(len != 0 && addr!=0){
    int onceLength = sizeof(word_t) < len ? sizeof(word_t) : (len>>1)<<1;
    if (onceLength == 0)
      onceLength = 1;
    
    if(!in_pmem(addr))
      addr += CONFIG_MBASE;
    if(in_pmem(addr)){
      int printCount = 0;
      printf("0x%x:\t", addr);
      for(int i = len;i > 0; i-=onceLength){
        word_t data = vaddr_read(addr, onceLength);
        printf("0x%016lx\t",data);
        if (++printCount % 4 == 0){
          printf("\n");
        }
        addr+=onceLength;
        while (onceLength > i)
          onceLength/=2;
      }
      printf("\n");
    }
    else
      printf("%s\n", ANSI_FMT(str(Error: valid mem address.), ANSI_FG_RED));
    return 0;
  }
  printf("%s\n", ANSI_FMT(str(Error: valid mem address.), ANSI_FG_RED));
  return 0;
}

static int cmd_p(char *args){
  if(args){
    bool isSuccess = false;
    word_t res = expr(args, &isSuccess);
    if(isSuccess){
      printf("expr %s, value is %lu (0x%lx)\n",args,res,res);
    }
    else{
      printf("%s\n", ANSI_FMT(str(Error: Valit expr), ANSI_FG_RED));
      return 0;
    }
  }
  return 0;
}

static int cmd_w(char *args){
  if(args){
    WP *wp = new_wp(args);
    if (wp==NULL)
      printf("%s\n", ANSI_FMT(str(Error: Valit expr), ANSI_FG_RED));
  }
  return 0;
}

static int cmd_b(char *args){
  if(args){
    char ch[50] = " ";
    if (args[0]=='0'&&args[1]=='x')
      sprintf(ch, "$pc == %s",args);
    else
      sprintf(ch, "$pc == 0x%s",args);
    WP *wp = new_wp(ch);
    if (wp==NULL)
      printf("Valid expr %s\n", args);
  }
  return 0;
}

static int cmd_d(char *args){
  if(args){
    int wp_id = atoi(args);
    delete_wp(wp_id);
  }
  return 0;
}

static int cmd_gdb(char *args){
  printf("\n");
  return 0;
}

static struct {
  const char *name;
  const char *description;
  int (*handler) (char *);
} cmd_table [] = {
  { "help", "Display information about all supported commands", cmd_help },
  { "c", "Continue the execution of the program", cmd_c },
  { "q", "Exit NEMU", cmd_q },

  /* TODO: Add more commands */
  {"si", "si [N], step in the program", cmd_si},
  {"info", "info [r|w], out put the info of Regesiter or WatchPoint", cmd_info},
  {"x", "x [N] [EXPR] , out put N Bite data from value of EXPR by sixteen format", cmd_x},
  {"p", "p [EXPR], out put the value of EXPR", cmd_p},
  {"w", "w [EXPR], set WatchPoint stop the program if the value of EXPR has changed", cmd_w},
  {"d", "d [N], delete WatchPoint witch id is N", cmd_d},
  {"b", "b [EXPR], set breakoint at address of EXPR", cmd_b},
  {"g", "an empty command for exit into gdb", cmd_gdb},
};

#define NR_CMD ARRLEN(cmd_table)

static int cmd_help(char *args) {
  /* extract the first argument */
  char *arg = strtok(NULL, " ");
  int i;

  if (arg == NULL) {
    /* no argument given */
    for (i = 0; i < NR_CMD; i ++) {
      printf("%s - %s\n", cmd_table[i].name, cmd_table[i].description);
    }
  }
  else {
    for (i = 0; i < NR_CMD; i ++) {
      if (strcmp(arg, cmd_table[i].name) == 0) {
        printf("%s - %s\n", cmd_table[i].name, cmd_table[i].description);
        return 0;
      }
    }
    printf("Unknown command '%s'\n", arg);
  }
  return 0;
}

void sdb_set_batch_mode() {
  is_batch_mode = true;
}

void sdb_mainloop() {
  if (is_batch_mode) {
    cmd_c(NULL);
    return;
  }

  for (char *str; (str = rl_gets()) != NULL; ) {
    char *str_end = str + strlen(str);

    /* extract the first token as the command */
    char *cmd = strtok(str, " ");
    if (cmd == NULL) { continue; }

    /* treat the remaining string as the arguments,
     * which may need further parsing
     */
    char *args = cmd + strlen(cmd) + 1;
    if (args >= str_end) {
      args = NULL;
    }

#ifdef CONFIG_DEVICE
    extern void sdl_clear_event_queue();
    sdl_clear_event_queue();
#endif

    int i;
    for (i = 0; i < NR_CMD; i ++) {
      if (strcmp(cmd, cmd_table[i].name) == 0) {
        if (cmd_table[i].handler(args) < 0) { nemu_state.state = NEMU_QUIT; return; }
        break;
      }
    }

    if (i == NR_CMD) { printf("Unknown command '%s'\n", cmd); }
  }
}

void init_sdb() {
  /* Compile the regular expressions. */
  init_regex();

  /* Initialize the watchpoint pool. */
  init_wp_pool();
}
