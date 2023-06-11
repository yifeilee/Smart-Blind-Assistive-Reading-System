%module mytext 

%include "std_string.i"

%{
#include "mytext.h"
%}

char *alphabet2byte(char* str, int size);
