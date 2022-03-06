#include "sdb.h"

#define NR_WP 32

typedef struct watchpoint {
  int NO;
  struct watchpoint *next;
  struct watchpoint *prev;

  bool is_free;

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
  }

  head = NULL;
  tail = NULL;
  free_ = wp_pool;
}

/* TODO: Implement the functionality of watchpoint */

WP* new_wp(){
  WP * newWp;
  if(free_ == NULL)
    return NULL;
    
  newWp = free_;
  free_ = free_->next;
  free_->prev = newWp->prev;

  newWp->next = NULL;
  newWp->prev = NULL;
  newWp->is_free = false;

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
  if(wp->is_free)
    return;

  if (wp->prev == NULL)
    head = wp->next;
  else
    wp->prev->next = wp->next;
  
  if(wp->next == NULL)
    tail = wp->prev;
  else
    wp->next->prev = wp->prev;

  wp->next = free_;
  wp->prev = NULL;
  wp->is_free = true;
  free_ = wp;

  return;
}