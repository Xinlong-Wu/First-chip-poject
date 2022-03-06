#include <isa.h>

/* We use the POSIX regex functions to process regular expressions.
 * Type 'man regex' for more information about POSIX regex functions.
 */
#include <regex.h>
#include <utils.h>
#include <memory/vaddr.h>
#include <memory/paddr.h>

enum {
  TK_EQ,
  TK_NEQ,
  TK_AND,
  TK_NOTYPE = 256,
  TK_NUM,
  TK_NEG_NUM,
  TK_HEX_NUM,
  TK_REG,
  TK_DEREF,
};

static struct rule {
  const char *regex;
  int token_type;
} rules[] = {

  /* TODO: Add more rules.
   * Pay attention to the precedence level of different rules.
   */
  {"0[x|X][0-9|a-f]+", TK_HEX_NUM},
  {"\\$.*", TK_REG},
  {" +", TK_NOTYPE},    // spaces
  {"\\+", '+'},         // plus
  {"\\-", '-'},         // minus
  {"\\*", '*'},         // mul
  {"/", '/'},         // div
  {"\\(", '('},         // Left parenthesis
  {"\\)", ')'},         // right parenthesis
  {"==", TK_EQ},        // equal
  {"!=", TK_NEQ},       // not equal
  {"&&", TK_AND},       // and
  {"[0-9]+", TK_NUM}       //number
};

#define NR_REGEX ARRLEN(rules)

static regex_t re[NR_REGEX] = {};

/* Rules are used for many times.
 * Therefore we compile them only once before any usage.
 */
void init_regex() {
  int i;
  char error_msg[128];
  int ret;

  for (i = 0; i < NR_REGEX; i ++) {
    ret = regcomp(&re[i], rules[i].regex, REG_EXTENDED);
    if (ret != 0) {
      regerror(ret, &re[i], error_msg, 128);
      panic("regex compilation failed: %s\n%s", error_msg, rules[i].regex);
    }
  }
}

typedef struct token {
  int type;
  char str[32];
} Token;

static Token tokens[32] __attribute__((used)) = {};
static int nr_token __attribute__((used))  = 0;

static bool make_token(char *e) {
  int position = 0;
  int i;
  regmatch_t pmatch;

  nr_token = 0;

  while (e[position] != '\0') {
    /* Try all rules one by one. */
    for (i = 0; i < NR_REGEX; i ++) {
      if (regexec(&re[i], e + position, 1, &pmatch, 0) == 0 && pmatch.rm_so == 0) {
        char *substr_start = e + position;
        int substr_len = pmatch.rm_eo;

        Log("match rules[%d] = \"%s\" at position %d with len %d: %.*s",
            i, rules[i].regex, position, substr_len, substr_len, substr_start);

        position += substr_len;

        /* TODO: Now a new token is recognized with rules[i]. Add codes
         * to record the token in the array `tokens'. For certain types
         * of tokens, some extra actions should be performed.
         */

        switch (rules[i].token_type) {
          case '+':
          case '-':
          case '*':
          case '/':
          case '(':
          case ')':
          case TK_EQ:
          case TK_NEQ:
          case TK_AND:
            tokens[nr_token].type = rules[i].token_type;
            tokens[nr_token].str[0] = rules[i].token_type;
            nr_token++;
            break;
          case TK_NUM:
          case TK_HEX_NUM:
          case TK_REG:
            assert(substr_len < 32 && "Number input is too long");
            if (substr_len > 32){
              Log("Number input is too long");
              return false;
            }
            tokens[nr_token].type = rules[i].token_type;
            for (size_t i = 0; i < substr_len; i++){
              tokens[nr_token].str[i] = substr_start[i];
            }
            nr_token++;
            break;
          default:
            break;
        }

        break;
      }
    }

    if (i == NR_REGEX) {
      printf("no match at position %d\n%s\n%*.s^\n", position, e, position, "");
      return false;
    }
  }
  return true;
}

word_t eval(int p, int q, bool *success);

word_t expr(char *e, bool *success) {
  if (!make_token(e)) {
    *success = false;
    return 0;
  }

  // recognize the negative number
  for (size_t i = 0; i < nr_token; i++){
    switch (tokens[i].type){
      default:
        break;
      case '-':{
        if (tokens[i-1].type < TK_NOTYPE)
          tokens[i].type = TK_NEG_NUM;
        break;
      }
      case '*':{
        if (tokens[i-1].type < TK_NOTYPE)
          tokens[i].type = TK_DEREF;
        break;
      }
    }
  }
  

  word_t res = eval(0,nr_token-1, success);

  return res;
}

bool check_parentheses(int p, int q){
  int unmatched_parenthesis = 0;
  for(int i = p; i<=q;i++){
    if(tokens[i].type == '(')
      unmatched_parenthesis+=1;
    else if(tokens[i].type == ')')
      unmatched_parenthesis-=1;
  }
  return unmatched_parenthesis == 0;
}

int get_priority(int * base_priority, int token_type){
  switch (token_type)
  {
    case TK_AND:
      return (*base_priority) + 0;
    case TK_EQ:
    case TK_NEQ:
      return (*base_priority) + 1;
    case '+':
    case '-':
      return (*base_priority) + 2;
    case '*':
    case '/':
      return (*base_priority) + 3;
    case '(':
      (*base_priority)+=100;
      return -1;
    case ')':
      (*base_priority)-=100;
      return -1;
    
    default:
      return -1;
  }
}

word_t eval(int p, int q, bool *success){
  if (p > q) {
    /* Bad expression */
    Log(" Bad expression ");
    *success = false;
    return 0;
  }
  else if (tokens[p].type == '(' && tokens[q].type == ')') {
    /* The expression is surrounded by a matched pair of parentheses.
     * If that is the case, just throw away the parentheses.
     */
    return eval(p + 1, q - 1, success);
  }
  else if (p == q) {
    /* Single token.
     * For now this token should be a number.
     * Return the value of the number.
     */
    switch (tokens[p].type){
      default:{
        // Log("Current Token is %c",tokens[p].type);
        *success = false;
        return 0;
      }
      case TK_NUM:{
        // Log("Current Token is number %s, convert to int %d",tokens[p].str,atoi(tokens[p].str));
        *success = true;
        return atoi(tokens[p].str);
      }
      case TK_HEX_NUM:{
        // Log("Current Token is number %s, convert to int %d",tokens[p].str,htoi(tokens[p].str));
        *success = true;
        return htoi(tokens[p].str);
      }
      case TK_REG:{
        return isa_reg_str2val(tokens[p].str+1, success);
      }
    }
  }
  else {
    // handle special Token
    switch (tokens[p].type){
      default:
        break;
      case TK_NEG_NUM:{
        sword_t val = eval(p+1, q,success);
        return val*-1;
      }
      case TK_DEREF:{
        word_t addr = eval(p+1, q,success);
        if(!in_pmem(addr))
          addr += CONFIG_MBASE;
        printf("dereference 0x%016lx\n",addr);
        return vaddr_read(addr, 8);
      }
    }

    int base_priority = 0;
    int main_op = -1;
    int main_op_priority = -1;

    
    for (int i = p; i <= q; i++){
      if(tokens[i].type < TK_NOTYPE){
        if(main_op == -1 || main_op_priority == -1){
          main_op = i;
          main_op_priority = get_priority(&base_priority, tokens[i].type);
          continue;
        }

        int tmp_priority = get_priority(&base_priority, tokens[i].type);
        
        if(tmp_priority >= 0 && main_op_priority >= tmp_priority){
          main_op = i;
          main_op_priority = tmp_priority;
        }
      }
    }

    if(main_op < 1){
      *success = false;
      return 0;
    }

    // Log("main op is %c", tokens[main_op].type);

      // printf("print left Tokens: ");
      // for (size_t i = 0; i < main_op; i++)
      // {
      //   printf("%s ",tokens[i].str);
      // }
      // printf("\n");

      // printf("print right Tokens: ");
      // for (size_t i = main_op+1; i < nr_token; i++)
      // {
      //   printf("%s ",tokens[i].str);
      // }
      // printf("\n");
    
    bool val1_success = false;
    bool val2_success = false;

    sword_t val1 = eval(p, main_op - 1,&val1_success);
    // printf("res is %d\n", val1);
    sword_t val2 = eval(main_op + 1, q,&val2_success);
    // printf("res is %d\n", val2);

    *success = val1_success && val2_success;

    if(*success == false){
      return 0;
    }

    switch (tokens[main_op].type) {
      case '+': return val1 + val2;
      case '-': return val1 - val2;
      case '*': return val1 * val2;
      case '/': {
        if (val2 == 0){
          *success = false;
          return 0;
        }
        return val1 / val2;
      }
      case TK_EQ: return val1 == val2;
      case TK_NEQ: return val1 != val2;
      case TK_AND: return val1 && val2;
      default: *success = false; return 0;
    }
  }
}
