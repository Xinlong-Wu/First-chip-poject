#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>
#include <string.h>

// this should be enough
static int deep = 0;
static char buf[65536] = {};
static char* buf_pointer = buf;
static char code_buf[65536 + 128] = {}; // a little larger than `buf`
static char *code_format =
"#include <stdio.h>\n"
"int main() { "
"  unsigned result = %s; "
"  printf(\"%%u\", result); "
"  return 0; "
"}";

static uint32_t choose(uint32_t i) {
  srand((unsigned)time(NULL));
  return rand() % i;
}

static void write_buf(char *s) {
  for (size_t i = 0; i < strlen(s); i++){
    *buf_pointer = s[i];
    buf_pointer++;
  }
}

static void gen_num() {
  srand((unsigned)time(NULL));
  char tmp[32];
  sprintf(tmp,"%d",rand()%100000);
  write_buf(tmp);
}

static void gen_rand_op() {
  switch (choose(4)) {
    case 0: write_buf("+");
    case 1: write_buf("-");
    case 2: write_buf("*");
    case 3: write_buf("/");
    default: write_buf("+");
  }
}

static void gen(char *s) {
  int is_gen = choose(1);
  write_buf(is_gen ? s : " ");
}

static void gen_rand_expr() {
  switch (choose(3)) {
    case 0: gen_num(); break;
    case 1: 
      gen("("); 
      if(deep++ < 25)
        gen_rand_expr(); 
      else
        gen_num();
      gen(")"); 
      break;
    default: 
      if(deep++ < 25)
        gen_rand_expr(); 
      gen_rand_op(); 
      if(deep++ < 25)
        gen_rand_expr(); 
      break;
  }
}

int main(int argc, char *argv[]) {
  int seed = time(0);
  srand(seed);
  int loop = 1;
  if (argc > 1) {
    sscanf(argv[1], "%d", &loop);
  }
  int i;
  for (i = 0; i < loop; i ++) {
    gen_rand_expr();
    buf_pointer = buf;
    deep = 0;

    sprintf(code_buf, code_format, buf);

    FILE *fp = fopen("/tmp/.code.c", "w");
    assert(fp != NULL);
    fputs(code_buf, fp);
    fclose(fp);

    int ret = system("gcc /tmp/.code.c -o /tmp/.expr");
    if (ret != 0) continue;

    fp = popen("/tmp/.expr", "r");
    assert(fp != NULL);

    int result;
    fscanf(fp, "%d", &result);
    pclose(fp);

    printf("%u %s\n", result, buf);
  }
  return 0;
}
