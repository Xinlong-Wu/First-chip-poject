#ifndef __SDB_H__
#define __SDB_H__

#include <common.h>
#include <cpu/decode.h>

typedef struct watchpoint {
  int NO;
  struct watchpoint *next;
  struct watchpoint *prev;

  bool is_free;
  char expr[100];
  word_t expr_value;
  word_t new_value;

} WP;

extern bool delete_wp(int id);
extern WP* new_wp(char *exp);
extern WP * get_wp_list();

extern void exec_once(Decode *s, vaddr_t pc);

extern word_t vaddr_read(vaddr_t addr, int len);

word_t expr(char *e, bool *success);

extern void disassemble(char *str, int size, uint64_t pc, uint8_t *code, int nbyte);
#endif
