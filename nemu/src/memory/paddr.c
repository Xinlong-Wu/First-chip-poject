#include <memory/host.h>
#include <memory/paddr.h>
#include <device/mmio.h>
#include <isa.h>

extern int ftrace_sp;

#if   defined(CONFIG_PMEM_MALLOC)
static uint8_t *pmem = NULL;
#else // CONFIG_PMEM_GARRAY
static uint8_t pmem[CONFIG_MSIZE] PG_ALIGN = {};
#endif

uint8_t* guest_to_host(paddr_t paddr) { return pmem + paddr - CONFIG_MBASE; }
paddr_t host_to_guest(uint8_t *haddr) { return haddr - pmem + CONFIG_MBASE; }

static word_t pmem_read(paddr_t addr, int len) {
  word_t ret = host_read(guest_to_host(addr), len);
  return ret;
}

static void pmem_write(paddr_t addr, int len, word_t data) {
  host_write(guest_to_host(addr), len, data);
}

static void out_of_bound(paddr_t addr) {
  panic("address = " FMT_PADDR " is out of bound of pmem [" FMT_PADDR ", " FMT_PADDR ") at pc = " FMT_WORD,
      addr, CONFIG_MBASE, CONFIG_MBASE + CONFIG_MSIZE, cpu.pc);
}

void init_mem() {
#if   defined(CONFIG_PMEM_MALLOC)
  pmem = malloc(CONFIG_MSIZE);
  assert(pmem);
#endif
#ifdef CONFIG_MEM_RANDOM
  uint32_t *p = (uint32_t *)pmem;
  int i;
  for (i = 0; i < (int) (CONFIG_MSIZE / sizeof(p[0])); i ++) {
    p[i] = rand();
  }
#endif
  Log("physical memory area [" FMT_PADDR ", " FMT_PADDR "]",
      (paddr_t)CONFIG_MBASE, (paddr_t)CONFIG_MBASE + CONFIG_MSIZE);
}

word_t paddr_read(paddr_t addr, int len) {
#ifdef CONFIG_FTRACE
  ftrace_indent(ftrace_sp+3);
#endif
#ifdef CONFIG_TRACE_MEM
  log_write("Read Memory: *0x%x ", addr);
#endif
  if (likely(in_pmem(addr))) {
    word_t data = pmem_read(addr, len);
#ifdef CONFIG_TRACE_MEM
  log_write("=> [ 0x%08lx ], length = %d\n", data, len * 8);
#endif
    return data;
  }
  IFDEF(CONFIG_DEVICE, return mmio_read(addr, len));
#ifdef CONFIG_TRACE_MEM
  log_write(" is out of bound.\n");
#endif
  out_of_bound(addr);
  return 0;
}

void paddr_write(paddr_t addr, int len, word_t data) {
#ifdef CONFIG_FTRACE
  ftrace_indent(ftrace_sp+3);
#endif
#ifdef CONFIG_TRACE_MEM
  log_write("Write Memory: *0x%x ", addr);
#endif
  if (likely(in_pmem(addr))) { 
#ifdef CONFIG_TRACE_MEM
  log_write("<= [ 0x%08lx ], length = %d\n", data, len * 8);
#endif
    pmem_write(addr, len, data); 
    return;
  }
  IFDEF(CONFIG_DEVICE, mmio_write(addr, len, data); return);
#ifdef CONFIG_TRACE_MEM
  log_write(" is out of bound.\n");
#endif
  out_of_bound(addr);
}
