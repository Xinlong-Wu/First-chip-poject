#ifndef __SDB_H__
#define __SDB_H__

#include <common.h>

typedef struct watchpoint {
  int NO;
  struct watchpoint *next;
  struct watchpoint *prev;

  bool is_free;
  char expr[100];
  word_t expr_value;
  word_t new_value;

} WP;

word_t expr(char *e, bool *success);

#endif
