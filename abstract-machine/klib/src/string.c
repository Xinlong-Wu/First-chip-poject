#include <klib.h>
#include <klib-macros.h>
#include <stdint.h>

#if !defined(__ISA_NATIVE__) || defined(__NATIVE_USE_KLIB__)

size_t strlen(const char *s) {
  size_t len = 0;
  const char * p = s;
  while(*p!='\0'){
    len++;
    p++;
  }
  return len;
}

char *strcpy(char *dst, const char *src) {
  size_t p = 0;
  do
  {
    dst[p] = src[p];
    p++;
  } while (src[p]!='\0');
  return dst;
}

char *strncpy(char *dst, const char *src, size_t n) {
  size_t p = 0;

  if(n == 0)
    return dst;

  do
  {
    dst[p] = src[p];
    p++;
  } while (src[p]!='\0');

  while (p < n){
    p++;
    dst[p] = '\0';
  }

  return dst;
}

char *strcat(char *dst, const char *src) {
  size_t p = strlen(dst);
  do
  {
    dst[p] = *src;
    src = src + 1;
	p++;
  } while (*src!='\0');
  return dst;
}

int strcmp(const char *s1, const char *s2) {
  size_t p = 0;
  do
  {
    if((s1[p] - s2[p]) != 0)
      return (s1[p] - s2[p]);
    p++;
  } while (s1[p] != '\0');
  return 0;
}

int strncmp(const char *s1, const char *s2, size_t n) {
  size_t p = 0;

if (n == 0)
{
  return 0;
}


  do
  {
    if((s1[p] - s2[p]) != 0)
      return (s1[p] - s2[p]);
    p++;
  } while (s1[p] != '\0' && p < n);
  return 0;
}

void *memset(void *s, int c, size_t n) {
  const unsigned char uc = c;
  unsigned char *su;
  for(su = s;0 < n;++su,--n)
    *su = uc;
  return s;
}

void *memmove(void *dst, const void *src, size_t n) {
  void* ret = dst;
  if (dst <= src || (char*)dst >= ((char*)src+n) )
  {
    while(n--){
      *(char*) dst = *(char*)src;
      dst = (char*)dst + 1;
      src = (char*)src + 1;
    }
  }
  else{
    dst = (char*)dst + n - 1;
    src = (char*)src + n - 1;
    while(n--){
      *(char*) dst = *(char*)src;
      dst = (char*)dst - 1;
      src = (char*)src - 1;
    }
  }
  return ret;
}

void *memcpy(void *out, const void *in, size_t n) {
  void* ret = out;
  while(n--){
    *(char*) out = *(char*)in;
    out = (char*)out + 1;
    in = (char*)in + 1;
  }
  return ret;
}

int memcmp(const void *s1, const void *s2, size_t n) {
  size_t p = 0;
  while(p < n)
  {
    if((*((char*)s1+p) - *((char*)s2+p)) != 0)
      return (*(char*)s1 - *(char*)s2);
    
    p++;
  };
  return 0;
}

#endif
