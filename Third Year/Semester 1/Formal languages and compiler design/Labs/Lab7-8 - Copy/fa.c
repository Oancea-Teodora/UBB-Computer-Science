#include "fa.h"
#include <ctype.h>

/* Optimized: Use standard library functions for better performance */
int fa_ident_accept(const char* s){
    if(!s || !*s) return 0;
    /* Optimized: Use isalpha instead of manual range checks */
    if(!isalpha((unsigned char)s[0])) return 0;
    for(const char* p=s+1; *p; ++p){
        /* Optimized: Use isalnum instead of multiple range checks */
        if(!isalnum((unsigned char)*p) && *p != '_')
            return 0;
    }
    return 1;
}

/* Optimized: Use isdigit for better readability and potential compiler optimizations */
int fa_number_accept(const char* s){
    if(!s || !*s) return 0;
    const char* p=s;
    int digits_before=0, digits_after=0;

    /* Optimized: Use isdigit instead of manual range check */
    while(isdigit((unsigned char)*p)){ ++p; ++digits_before; }
    if(digits_before==0) return 0;

    if(*p=='.'){
        ++p;
        while(isdigit((unsigned char)*p)){ ++p; ++digits_after; }
        if(digits_after==0) return 0;
    }
    return *p=='\0';
}
