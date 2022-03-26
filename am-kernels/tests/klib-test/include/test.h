#ifdef __ARCH_NATIVE
#include<assert.h>
#define check assert
#else
#include "trap.h"
#endif