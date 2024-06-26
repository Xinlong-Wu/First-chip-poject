
#ifndef __WATCHPOINT_H__
#define __WATCHPOINT_H__

#define NR_WP 32

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

#endif