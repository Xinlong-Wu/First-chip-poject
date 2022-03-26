#include <isa.h>
#include <memory/paddr.h>

void init_rand();
void init_log(const char *log_file);
void init_mem();
void init_difftest(char *ref_so_file, long img_size, int port);
void init_device();
void init_sdb();
void init_disasm(const char *triple);

// ftrace
#ifdef CONFIG_FTRACE
#include <elf.h>
static void init_ftrace();
typedef struct symrecord{
	uint64_t addr;
	uint32_t szie;
	char name[50];
} symrecord;

size_t record_size = 0;
symrecord FuncRecord[100];
#endif

static void welcome() {
  Log("Trace: %s", MUXDEF(CONFIG_TRACE, ASNI_FMT("ON", ASNI_FG_GREEN), ASNI_FMT("OFF", ASNI_FG_RED)));
  IFDEF(CONFIG_TRACE, Log("If trace is enabled, a log file will be generated "
        "to record the trace. This may lead to a large log file. "
        "If it is not necessary, you can disable it in menuconfig"));
  Log("Build time: %s, %s", __TIME__, __DATE__);
  printf("Welcome to %s-NEMU!\n", ASNI_FMT(str(__GUEST_ISA__), ASNI_FG_YELLOW ASNI_BG_RED));
  printf("For help, type \"help\"\n");
  // Log("Exercise: Please remove me in the source code and compile NEMU again.");
  // assert(0);
}

#ifndef CONFIG_TARGET_AM
#include <getopt.h>

void sdb_set_batch_mode();

static char *log_file = NULL;
static char *diff_so_file = NULL;
static char *img_file = NULL;
static int difftest_port = 1234;
#ifdef CONFIG_FTRACE
static char *elf_file = NULL;
#endif

static long load_img() {
  if (img_file == NULL) {
    Log("No image is given. Use the default build-in image.");
    return 4096; // built-in image size
  }

  FILE *fp = fopen(img_file, "rb");
  Assert(fp, "Can not open '%s'", img_file);

  fseek(fp, 0, SEEK_END);
  long size = ftell(fp);

  Log("The image is %s, size = %ld", img_file, size);

  fseek(fp, 0, SEEK_SET);
  int ret = fread(guest_to_host(RESET_VECTOR), size, 1, fp);
  assert(ret == 1);

  fclose(fp);
  return size;
}

#ifdef CONFIG_FTRACE
static void init_ftrace(){
  if(elf_file == NULL)
    return;
  Elf64_Shdr strtab;
	Elf64_Shdr symtab;
  FILE * file;
  file = fopen(elf_file, "rb" );

  // read RLF header
  Elf64_Ehdr ehdr;
	fread(&ehdr, sizeof(Elf64_Ehdr),1,file);

  // read section header table
	Elf64_Shdr *shdr = malloc(sizeof(Elf64_Shdr)*ehdr.e_shnum);
	fseek(file, ehdr.e_shoff,SEEK_SET);
	fread(shdr, sizeof(Elf64_Shdr),ehdr.e_shnum,file);

  // select the .strtab & .symtab section
  Elf64_Shdr shstrtab = shdr[ehdr.e_shstrndx];
	char *sh_str = malloc(shstrtab.sh_size);
	fseek(file, shstrtab.sh_offset,SEEK_SET);
	fread(sh_str, shstrtab.sh_size,1,file);
	for (size_t i = 0; i < ehdr.e_shnum; i++){
		char *name = sh_str + shdr[i].sh_name;
		if(strcmp(name,".strtab")==0){
			strtab = shdr[i];
		}
		else if(strcmp(name,".symtab")==0){
			symtab = shdr[i];
		}
	}

  //read string pool
	char *str_pool = malloc(strtab.sh_size);
	fseek(file, strtab.sh_offset,SEEK_SET);
	fread(str_pool, strtab.sh_size,1,file);

  // read smybles.
	Elf64_Sym *syms = malloc(symtab.sh_size);
	fseek(file, symtab.sh_offset,SEEK_SET);
	fread(syms, symtab.sh_size,1,file);
  record_size = 0;
	for (size_t i = 0; i < symtab.sh_size/symtab.sh_entsize; i++){
		if(ELF64_ST_TYPE(syms[i].st_info)==STT_FUNC){
			FuncRecord[record_size].addr = syms[i].st_value;
			FuncRecord[record_size].szie = syms[i].st_size;
			strcpy(FuncRecord[record_size].name,str_pool+syms[i].st_name);
			record_size++;
		}
	}

  for (size_t i = 0; i < record_size; i++){
		printf("test:\t[%s@%lx]\n",FuncRecord[i].name,FuncRecord[i].addr);
	}

  free(sh_str);
  free(str_pool);
  free(syms);
	free(shdr);
  return;
}
#endif

static int parse_args(int argc, char *argv[]) {
  const struct option table[] = {
    {"batch"    , no_argument      , NULL, 'b'},
    {"log"      , required_argument, NULL, 'l'},
    {"diff"     , required_argument, NULL, 'd'},
    {"port"     , required_argument, NULL, 'p'},
    {"elf"      , required_argument, NULL, 'e'},
    {"help"     , no_argument      , NULL, 'h'},
    {0          , 0                , NULL,  0 },
  };
  int o;
  while ( (o = getopt_long(argc, argv, "-bhl:d:p:", table, NULL)) != -1) {
    switch (o) {
      case 'b': sdb_set_batch_mode(); break;
      case 'p': sscanf(optarg, "%d", &difftest_port); break;
      case 'l': log_file = optarg; break;
      case 'd': diff_so_file = optarg; break;
      case 'e': elf_file = optarg; break;
      case 1: img_file = optarg; return optind - 1;
      default:
        printf("Usage: %s [OPTION...] IMAGE [args]\n\n", argv[0]);
        printf("\t-b,--batch              run with batch mode\n");
        printf("\t-l,--log=FILE           output log to FILE\n");
        printf("\t-d,--diff=REF_SO        run DiffTest with reference REF_SO\n");
        printf("\t-e,--elf=bin.elf        elf file for function trace\n");
        printf("\t-p,--port=PORT          run DiffTest with port PORT\n");
        printf("\n");
        exit(0);
    }
  }
  return 0;
}

void init_monitor(int argc, char *argv[]) {
  /* Perform some global initialization. */

  /* Parse arguments. */
  parse_args(argc, argv);

  /* Set random seed. */
  init_rand();

  /* Open the log file. */
  init_log(log_file);

  /* Initialize memory. */
  init_mem();

  /* Initialize devices. */
  IFDEF(CONFIG_DEVICE, init_device());

  /* Perform ISA dependent initialization. */
  init_isa();

  /* Load the image to memory. This will overwrite the built-in image. */
  long img_size = load_img();

#ifdef CONFIG_FTRACE
  init_ftrace();
#endif

  /* Initialize differential testing. */
  init_difftest(diff_so_file, img_size, difftest_port);

  /* Initialize the simple debugger. */
  init_sdb();

  IFDEF(CONFIG_ITRACE, init_disasm(
    MUXDEF(CONFIG_ISA_x86,     "i686",
    MUXDEF(CONFIG_ISA_mips32,  "mipsel",
    MUXDEF(CONFIG_ISA_riscv32, "riscv32",
    MUXDEF(CONFIG_ISA_riscv64, "riscv64", "bad")))) "-pc-linux-gnu"
  ));

  /* Display welcome message. */
  welcome();
}
#else // CONFIG_TARGET_AM
static long load_img() {
  extern char bin_start, bin_end;
  size_t size = &bin_end - &bin_start;
  Log("img size = %ld", size);
  memcpy(guest_to_host(RESET_VECTOR), &bin_start, size);
  return size;
}

void am_init_monitor() {
  init_rand();
  init_mem();
  init_isa();
  load_img();
  IFDEF(CONFIG_DEVICE, init_device());
  welcome();
}
#endif
