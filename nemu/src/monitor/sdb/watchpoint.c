#include "sdb.h"

#define NR_WP 32

static WP wp_pool[NR_WP] = {};
static WP *head = NULL, *tail = NULL, *free_ = NULL;
static int active_wp = -1;

void init_wp_pool() {
  int i;
  for (i = 0; i < NR_WP; i ++) {
    wp_pool[i].NO = i;
    wp_pool[i].next = (i == NR_WP - 1 ? NULL : &wp_pool[i + 1]);
    wp_pool[i].prev = (i == 0 ? NULL : &wp_pool[i-1]);
    wp_pool[i].is_free = true;
    wp_pool[i].expr[0] = '\0';
    wp_pool[i].expr_value = 0;
    wp_pool[i].new_value = 0;
  }

  head = NULL;
  tail = NULL;
  free_ = wp_pool;
}

/* TODO: Implement the functionality of watchpoint */

WP* new_wp(char *exp){
  WP * newWp;
  // if all watch points are in used, return with NULL
  if(free_ == NULL)
    return NULL;
    
  // pick first item from free list as new watch point
  newWp = free_;
  free_ = free_->next;
  free_->prev = newWp->prev;

  bool valid_expr = false;
  // init watch piont
  newWp->next = NULL;
  newWp->prev = NULL;
  newWp->is_free = false;
  strcpy(newWp->expr, exp);
  newWp->expr_value = expr(newWp->expr,&valid_expr);
  newWp->new_value = newWp->expr_value;
  if(!valid_expr)
    return NULL;

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

bool free_wp(WP *wp){
  // avoid double free
  if(wp->is_free)
    return false;

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
  wp->new_value = 0; 
  wp->expr_value = 0;
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
  return true;
}

bool check_wp(){
  if(active_wp >= 0)
    active_wp = -1;

  WP *p = head;
  while (p != NULL){
    bool secuss;
    p->expr_value = p->new_value;
    p->new_value = expr(p->expr,&secuss);
    if(p->new_value != p->expr_value){
      active_wp = p->NO;
      break;
    }
  }
  
  return active_wp >= 0;
}

bool delete_wp(int id){
  if (id < 32)
    return free_wp(&(wp_pool[id]));
  return false;
}

void print_active_wp(){
  printf("watch point No.%d: %s, value %lu changed to %lu",active_wp,
         wp_pool[active_wp].expr, wp_pool[active_wp].expr_value,
         wp_pool[active_wp].new_value);
}