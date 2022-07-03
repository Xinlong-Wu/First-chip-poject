#include <cstdlib>
#include <stdio.h>
#include <readline/readline.h>
#include <readline/history.h>

#include"include/sdb.h"
#include"include/utils.h"
#include"include/verilator.h"

void init_sdb() {
//   init_regex();
//   init_wp_pool();
}

/* We use the `readline' library to provide more flexibility to read from stdin. */
static char* rl_gets() {
  static char *line_read = NULL;

  if (line_read) {
    free(line_read);
    line_read = NULL;
  }

  line_read = readline("(npc) ");

  if (line_read && *line_read) {
    add_history(line_read);
  }

  return line_read;
}

void sdb_mainloop() {
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

    int i;
    for (i = 0; i < NR_CMD; i ++) {
      if (strcmp(cmd, cmd_table[i].name) == 0) {
        if (cmd_table[i].handler(args) < 0) { exit(0); }
        break;
      }
    }

    if (i == NR_CMD) { 
      printf("Unknown command '%s'\n", cmd); 
    }
  }
}


int cmd_c(char *args) {
  return npc_exec(-1);
}


int cmd_q(char *args) {
  return -1;
}

int cmd_help(char *args) {
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

int cmd_si(char *args){
  int n = 1;
  if(args)
    sscanf(args,"%x",&n);
  
  while (n--){
    single_cycle();
    // printf("%s\n", s.logbuf);
  }
  return 0;
}