#include "sdb.h"

#define NR_WP 32

typedef struct watchpoint {
  int NO;
  struct watchpoint *next;
  struct watchpoint *prev;

  bool is_free;
  char expr[100];

} WP;

static WP wp_pool[NR_WP] = {};
static WP *head = NULL, *tail = NULL, *free_ = NULL;

void init_wp_pool() {
  int i;
  for (i = 0; i < NR_WP; i ++) {
    wp_pool[i].NO = i;
    wp_pool[i].next = (i == NR_WP - 1 ? NULL : &wp_pool[i + 1]);
    wp_pool[i].prev = (i == 0 ? NULL : &wp_pool[i-1]);
    wp_pool[i].is_free = true;
    wp_pool[i].expr[0] = '\0';
  }

  head = NULL;
  tail = NULL;
  free_ = wp_pool;
}

/* TODO: Implement the functionality of watchpoint */

WP* new_wp(char *expr){
  WP * newWp;
  // if all watch points are in used, return with NULL
  if(free_ == NULL)
    return NULL;
    
  // pick first item from free list as new watch point
  newWp = free_;
  free_ = free_->next;
  free_->prev = newWp->prev;

  // init watch piont
  newWp->next = NULL;
  newWp->prev = NULL;
  newWp->is_free = false;
  strcpy(newWp->expr, expr);

  // append to watch point list
  if (head == NULL){
    head = newWp;
    tail = newWp;
  }
  else{
    tail->next = newWp;
    newWp->prev = tail;
    tail = newWp;
  }

  return newWp;
}

void free_wp(WP *wp){
  // avoid double free
  if(wp->is_free)
    return;

  // remove wp pointer from prev wp
  if (wp->prev == NULL)
    head = wp->next;
  else
    wp->prev->next = wp->next;
  
  // remove wp pointer from next wp
  if(wp->next == NULL)
    tail = wp->prev;
  else
    wp->next->prev = wp->prev;

  // free wp and append to free list
  wp->expr[0] = '\0';
  wp->is_free = true;
  wp->next = NULL;
  if(free_ == NULL){
    wp->prev = NULL;
    free_ = wp;
  }
  else{
    WP *p = free_;
    while (p->next != NULL)
      p = p->next;
    wp->prev = p;
    p->next = wp;
  }
  return;
}