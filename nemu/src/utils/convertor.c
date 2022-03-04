#include<utils.h>
#include <ctype.h>

word_t htoi(char *s){  
    word_t i;  
    word_t n = 0;  
    if (s[0] == '0' && (s[1]=='x' || s[1]=='X'))
        i = 2;  
    else
        i = 0;  
    for (; (s[i] >= '0' && s[i] <= '9') || (s[i] >= 'a' && s[i] <= 'z') || (s[i] >='A' && s[i] <= 'Z');++i)  {  
        if (tolower(s[i]) > '9')
            n = 16 * n + (10 + tolower(s[i]) - 'a');  
        else  
            n = 16 * n + (tolower(s[i]) - '0');  
    }  
    return n;  
}  