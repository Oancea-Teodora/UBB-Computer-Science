#include "fa.h"
#include <ctype.h>

int fa_ident_accept(const char* s){
    if(!s || !*s) return 0;
    if(!((s[0]>='A'&&s[0]<='Z')||(s[0]>='a'&&s[0]<='z'))) return 0;
    for(const char* p=s+1; *p; ++p){
        if( ( *p>='A'&&*p<='Z') || ( *p>='a'&&*p<='z') || ( *p>='0'&&*p<='9') || *p=='_' )
            continue;
        return 0;
    }
    return 1;
}

int fa_number_accept(const char* s){
    if(!s || !*s) return 0;
    const char* p=s;
    int digits_before=0, digits_after=0;

    while(*p>='0' && *p<='9'){ ++p; ++digits_before; }
    if(digits_before==0) return 0;

    if(*p=='.'){
        ++p;
        while(*p>='0' && *p<='9'){ ++p; ++digits_after; }
        if(digits_after==0) return 0;
    }
    return *p=='\0';
}
