/* It's an automatically generated code. Do not modify it. */
package com.nitnelav.metricas.halstead;
import static com.nitnelav.metricas.halstead.Tokens.*;

%%
%class Lexer
%type Tokens
L=[a-zA-ZÑñÁáÉéÍíÓóÚúÜü_]+
D=[0-9.]+
espacio=[ ,\t,\r,\n]+
comilla=[\",\']+
%{
    public String lexeme;
%}
%%"
print |
if |
else |
import |
input |
return |
while {lexeme=yytext(); return Operador;}
{espacio} {/*Ignore*/}
"#".* {/*Ignore*/}
"=" {return Operador;}
"+" {return Operador;}
"-" {return Operador;}
"*" {return Operador;}
"/" {return Operador;}
"<" {return Operador;}
">" {return Operador;}
{L}({L}|{D})* {lexeme=yytext(); return Operando;}
("(-"{D}+")")|{D}+ {lexeme=yytext(); return Operando;}
{comilla}{L}({L}|{D}|{espacio})*{comilla} {lexeme=yytext(); return Operando;}
 . {return ERROR;}