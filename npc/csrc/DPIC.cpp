#include<cstdio>

#include"include/DPIC.h"

bool is_ebreak = false;
int rval = 0;
void hit_ebreak(int res,svBit unimp){
  if (unimp)
  {
    printf("\n\033[1;31m hit unimp inst\033[0m\n");
    is_ebreak = true;
    rval = -1;
  }else 
  {
    printf("hit ebreak with value %d\n",res);
    is_ebreak = true;
    rval = res;
  }
}