/*************************************************************
* COMPILERS COURSE - Algonquin College
* Code version: Fall, 2020
*************************************************************
* File name: parser.h
* Compiler: MS Visual Studio 2019
* Author: Svillen Ranev
* Course: CST 8152 – Compilers, Lab Section: [011, 012, 013, 021, 022, or 023]
* Assignment: A3.
* Date: Sep 01 2020
* Professor: Paulo Sousa / Haider Miraj
* Purpose: This file is the main header for Parser (.h)
* Function list: (...).
*************************************************************/

/* Inclusion section */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

#ifndef PARSER_H_ 
#define PARSER_H_ 

#ifndef TOKEN_H_
#include "token.h"
#endif

#ifndef BUFFER_H_
#include "buffer.h"
#endif

/* Global vars */
static const char PROCESS_PARSER[] = "PLATY: Source file parsed";
static const char PROGRAM_PARSED[] = "PLATY: Program parsed";
static const char OPT_STATEMENTS[] = "PLATY: Opt_statements parsed";
static const char ASSIGNMENT_STATEMENT[] = "PLATY: Assignment statement parsed";
static const char SELECTION_STATEMENT[] = "PLATY: Selection statement parsed";
static const char ITERATION_STATEMENT[] = "PLATY: Iteration statement parsed";
static const char INPUT_STATEMENT[] = "PLATY: Input statement parsed";
static const char OUTPUT_STATEMENT[] = "PLATY: Output statement parsed";
static const char ARITHMETIC_ASSIGNMENT_EXPRESSION[] = "PLATY: Assignment expression (arithmetic) parsed";
static const char STRING_ASSIGNMENT_EXPRESSION[] = "PLATY: Assignment expression (string) parsed";
static const char ARITHMETIC_EXPRESSION[] = "PLATY: Arithmetic expression parsed";
static const char STRING_EXPRESSION[] = "PLATY: String expression parsed";
static const char CONDITIONAL_EXPRESSION[] = "PLATY: Conditional expression parsed";
static const char VARIABLE_LIST[] = "PLATY: Variable list parsed";
static const char OUTPUT_LIST[] = "PLATY: Output list (string literal) parsed";
static const char OPT_VARIABLE_LIST[] = "PLATY: Output list (empty) parsed";
static const char UNARY_ARITHMETIC_EXPRESSION[] = "PLATY: Unary arithmetic expression parsed";
static const char PRIMARY_ARITHMETIC_EXPRESSION[] = "PLATY: Primary arithmetic expression parsed";
static const char ADDITIVE_ARITHMETIC_EXPRESSION[] = "PLATY: Additive arithmetic expression parsed";
static const char MULTIPLICATIVE_ARITHMETIC_EXPRESSION[] = "PLATY: Multiplicative arithmetic expression parsed";
static const char PRIMARY_STRING_EXPRESSION[] = "PLATY: Primary string expression parsed";
static const char LOGICAL_OR_EXPRESSION[] = "PLATY: Logical OR expression parsed";
static const char LOGICAL_NOT_EXPRESSION[] = "PLATY: Logical NOT expression parsed";
static const char LOGICAL_AND_EXPRESSION[] = "PLATY: Logical AND expression parsed";
static const char RELATIONAL_EXPRESSION[] = "PLATY: Relational expression parsed";
static const char PRIMARY_A_RELATIONAL_EXPRESSION[] = "PLATY: Primary a_relational expression parsed";
static const char PRIMARY_S_RELATIONAL_EXPRESSION[] = "PLATY: Primary s_relational expression parsed";

typedef void(*PTR)(void);

static Token lookahead;
int syntaxErrorNumber;

extern Buffer* stringLiteralTable;
extern int line;
extern Token processToken();
extern char* keywordTable[];

/* Constants */
#define	NO_ATTR (-1)
#define DO 0
#define ELSE 1
#define FALSE 2
#define IF 3
#define INPUT 4
#define OUTPUT 5
#define PROGRAM 6
#define THEN 7
#define TRUE 8
#define WHILE 9

/* Function definitions */
void processParser(void);
void matchToken(int, int);
void syncErrorHandler(int);
void printError();

char comparator(int, int, ...);

void program(void);
void optionalStatements(void);
void statements(void);
void statement(void);
void statementsPrime(void);
void assignmentStatement(void);
void selectionStatement(void);
void iterationStatement(void);
void inputStatement(void);
void outputStatement(void);
void assignmentExpression(void);
void arithmeticExpression(void);
void stringExpression(void);
void preCondition(void);
void conditionalExpression(void);
void variableList(void);
void variableIdentifier(void);
void variableListPrime(void);
void outputList(void);
void optionalVariableList(void);
void unaryArithmeticExpression(void);
void additiveArithmeticExpression(void);
void primaryArithmeticExpression(void);
void multiplicativeArithmeticExpression(void);
void additiveArithmeticExpressionPrime(void);
void multiplicativeArithmeticExpressionPrime(void);
void primaryStringExpression(void);
void stringExpressionPrime(void);
void logicalOrExpression(void);
void logicalAndExpression(void);
void logicalOrExpressionPrime(void);
void logicalNotExpression(void);
void logicalAndExpressionPrime(void);
void relationalExpression(void);
void primaryARelationalExpression(void);
void primaryARelationalExpressionPrime(void);
void primarySRelationalExpression(void);
void primarySRelationalExpressionPrime(void);

#endif
