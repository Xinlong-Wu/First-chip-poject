#include<iostream>
#include<getopt.h>
#include<assert.h>

#include"include/sdb.h"
#include"include/verilator.h"
#include"include/DPIC.h"

static char *img_file = NULL;

static int parse_args(int argc, char *argv[]) {
  const struct option table[] = {
    // {"batch"    , no_argument      , NULL, 'b'},
    // {"log"      , required_argument, NULL, 'l'},
    // {"diff"     , required_argument, NULL, 'd'},
    // {"port"     , required_argument, NULL, 'p'},
    // {"elf"      , required_argument, NULL, 'e'},
    {"help"     , no_argument      , NULL, 'h'},
  };
  int o;
  while ( (o = getopt_long(argc, argv, "-bhl:d:p:", table, NULL)) != -1) {
    switch (o) {
      // case 'b': sdb_set_batch_mode(); break;
      // case 'p': sscanf(optarg, "%d", &difftest_port); break;
      // case 'l': log_file = optarg; break;
      // case 'd': diff_so_file = optarg; break;
      // case 'e': elf_file = optarg; break;
      case 1: img_file = optarg; return optind - 1;
      default:
        printf("Usage: %s [OPTION...] IMAGE [args]\n\n", argv[0]);
        // printf("\t-b,--batch              run with batch mode\n");
        // printf("\t-l,--log=FILE           output log to FILE\n");
        // printf("\t-d,--diff=REF_SO        run DiffTest with reference REF_SO\n");
        // printf("\t-e,--elf=bin.elf        elf file for function trace\n");
        // printf("\t-p,--port=PORT          run DiffTest with port PORT\n");
        printf("\n");
        exit(0);
    }
  }
  return 0;
}


uint32_t inst_rom[65536];
static long load_img() {
  FILE *fp = fopen(img_file, "rb");
  if( fp == NULL ) {
		printf( "Can not open this file!\n" );
		exit(1);
  }

  fseek(fp, 0, SEEK_END);
  long size = ftell(fp);

  fseek(fp, 0, SEEK_SET);
  int ret = fread(inst_rom, size, 1, fp);
  assert(ret == 1);

  fclose(fp);
  return size;
}

void init(int argc, char *argv[]){
  parse_args(argc,argv);

  long img_size = load_img();

  init_sdb();

  init_verilator(argc,argv);
}

void engine_start() {
  // sdb_mainloop();
}

int main(int argc, char **argv) {

  init(argc,argv);

  printf("\033[1;32m npc start \033[0m\n");
  sdb_mainloop();

  delete top;
  delete tfp;
  return rval;
}
