#include "parser.h"

static char debugMode = 0;
pBuffer stringLiteralTable;
PTR functionStatements[] = {
	NULL, NULL, NULL,
	selectionStatement,
	inputStatement,
	outputStatement,
	NULL, NULL, NULL,
	iterationStatement
};

/*************************************************************
 * Process Parser
 ************************************************************/
void processParser(void) 
{
	if (debugMode) { fprintf(stderr, "[00] Processing parser...\n"); }

	lookahead = processToken();
	program();
	matchToken(SEOF_T, NO_ATTR);
	fprintf(stdout, "%s\n", PROCESS_PARSER);
}

/*************************************************************
 * Match Token
 ************************************************************/
void matchToken(int tokenCode, int tokenAttribute) 
{
	if ((lookahead.code != tokenCode)
		|| (comparator(lookahead.code, 4, KW_T, REL_OP_T, ART_OP_T, LOG_OP_T)
		&& lookahead.attribute.get_int != tokenAttribute))
	{
		syncErrorHandler(tokenCode);
		return;
	}

	if (lookahead.code == SEOF_T) { return; }
	lookahead = processToken();
	if (lookahead.code == ERR_T)
	{
		printError();
		lookahead = processToken();
		++syntaxErrorNumber;
	}
}

/*************************************************************
 * Syncronize Error Handler
 ************************************************************/
void syncErrorHandler(int syncTokenCode) 
{
	printError();
	++syntaxErrorNumber;

	for (; lookahead.code != syncTokenCode; lookahead = processToken())
	{
		if (lookahead.code == SEOF_T) { exit(syntaxErrorNumber); }
	}

	lookahead = (lookahead.code != SEOF_T) ? processToken() : lookahead;
}

/*************************************************************
 * Comparator 
 ************************************************************/
char comparator(int code, int val, ...)
{
	int i;
	va_list valist;
	va_start(valist, val);
	for (i = 0; i < val; ++i)
	{
		if (code == va_arg(valist, int))
		{
			va_end(valist);
			return 1;
		}
	}
	return 0;
}


/*************************************************************
 * Print Error
 ************************************************************/
void printError() 
{
	Token t = lookahead;

	if (debugMode) { fprintf(stderr, "[39] PrintError\n"); }

	fprintf(stdout, "PLATY: Syntax error:  Line:%3d\n", line);
	fprintf(stdout, "*****  Token code:%3d Attribute: ", lookahead.code);

	if (t.code == ERR_T) { fprintf(stdout, "%s\n", t.attribute.err_lex); return; }

	if (comparator(t.code, 2, AVID_T, SVID_T)) { fprintf(stdout, "%s\n", t.attribute.vid_lex); return; }

	if (t.code == SEOF_T) { fprintf(stdout, "%s\t\t%d\t\n", "SEOF_T", t.attribute.get_int + 1); return; }/*TODO*/

	if (comparator(t.code, 9, SCC_OP_T, ASS_OP_T, LPR_T, RPR_T, LBR_T, RBR_T, COM_T, EOS_T))
	{ fprintf(stdout, "%s", "NA\n"); return; }

	if (t.code == FPL_T) { fprintf(stdout, "%5.1f\n", t.attribute.flt_value); return; }

	if (t.code == INL_T) { fprintf(stdout, "%d\n", t.attribute.int_value); return; }

	if (t.code == STR_T) { fprintf(stdout, "%s\n", bufferGetString(stringLiteralTable, t.attribute.str_offset)); return; }

	if (comparator(t.code, 3, ART_OP_T, REL_OP_T, LOG_OP_T)) { fprintf(stdout, "%d\n", t.attribute.get_int); return; }

	if (t.code == KW_T) { fprintf(stdout, "%s\n", keywordTable[t.attribute.get_int]); return; }

	fprintf(stdout, "%s%d%s", "PLATY: Scanner error: invalid token code: ", t.code, "\n");
}

/*************************************************************
 * Program statement
 * BNF: <program> -> PLATYPUS { <opt_statements> }
 * FIRST(<program>)= {KW_T (PLATYPUS)}.
 ************************************************************/
void program(void) 
{
	if (debugMode) { fprintf(stderr, "[01] Program\n"); }

	matchToken(KW_T, PROGRAM);
	matchToken(LBR_T, NO_ATTR);
	optionalStatements();
	matchToken(RBR_T, NO_ATTR);
	fprintf(stdout, "%s\n", PROGRAM_PARSED);
}

void optionalStatements(void)
{
	if (comparator(lookahead.code, 2, AVID_T, SVID_T)
		|| (lookahead.code == KW_T
			&& comparator(lookahead.attribute.get_int, 4, IF, WHILE, INPUT, OUTPUT)))
	{
		statements();
		return;
	}

	fprintf(stdout, "%s\n", OPT_STATEMENTS);
}

void statements(void)
{
	statement();
	statementsPrime();
}

void statement(void)
{
	if (comparator(lookahead.code, 2, AVID_T, SVID_T))
	{
		assignmentStatement();
		return;
	}

	if (lookahead.code == KW_T 
		&& comparator(lookahead.attribute.get_int, 4, IF, INPUT, OUTPUT, WHILE))
	{
		functionStatements[lookahead.attribute.get_int]();
		return;
	}

	printError();
}

void statementsPrime(void)
{
	if (comparator(lookahead.code, 2, AVID_T, SVID_T)
		|| (lookahead.code == KW_T &&
			comparator(lookahead.attribute.get_int, 4, IF, INPUT, OUTPUT, WHILE)))
	{
		statement();
		statementsPrime();
	}
}

void assignmentStatement(void)
{
	assignmentExpression();
	matchToken(EOS_T, NO_ATTR);
	fprintf(stdout, "%s\n", ASSIGNMENT_STATEMENT);
}

void selectionStatement(void)
{
	matchToken(KW_T, IF);
	preCondition();
	matchToken(LPR_T, NO_ATTR);
	conditionalExpression();
	matchToken(RPR_T, NO_ATTR);

	matchToken(KW_T, THEN);
	matchToken(LBR_T, NO_ATTR);
	optionalStatements();
	matchToken(RBR_T, NO_ATTR);

	matchToken(KW_T, ELSE);
	matchToken(LBR_T, NO_ATTR);
	optionalStatements();
	matchToken(RBR_T, NO_ATTR);
	matchToken(EOS_T, NO_ATTR);

	fprintf(stdout, "%s\n", SELECTION_STATEMENT);
}

void iterationStatement(void)
{
	matchToken(KW_T, WHILE);
	preCondition();
	matchToken(LPR_T, NO_ATTR);
	conditionalExpression();
	matchToken(RPR_T, NO_ATTR);

	matchToken(KW_T, DO);
	matchToken(LBR_T, NO_ATTR);
	statements();
	matchToken(RBR_T, NO_ATTR);
	matchToken(EOS_T, NO_ATTR);

	fprintf(stdout, "%s\n", ITERATION_STATEMENT);
}

void inputStatement(void)
{
	matchToken(KW_T, INPUT);
	matchToken(LPR_T, NO_ATTR);
	variableList();
	matchToken(RPR_T, NO_ATTR);
	matchToken(EOS_T, NO_ATTR);

	fprintf(stdout, "%s\n", INPUT_STATEMENT);
}

void outputStatement(void)
{
	matchToken(KW_T, OUTPUT);
	matchToken(LPR_T, NO_ATTR);
	outputList();
	matchToken(RPR_T, NO_ATTR);
	matchToken(EOS_T, NO_ATTR);

	fprintf(stdout, "%s\n", OUTPUT_STATEMENT);
}

void assignmentExpression(void)
{
	if (lookahead.code == AVID_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		matchToken(ASS_OP_T, NO_ATTR);
		arithmeticExpression();
		fprintf(stdout, "%s\n", ARITHMETIC_ASSIGNMENT_EXPRESSION);
		return;
	}

	if (lookahead.code == SVID_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		matchToken(ASS_OP_T, NO_ATTR);
		stringExpression();
		fprintf(stdout, "%s\n", STRING_ASSIGNMENT_EXPRESSION);
		return;
	}

	printError();
}

void arithmeticExpression(void)
{
	if (lookahead.code == ART_OP_T
		&& comparator(lookahead.attribute.arr_op, 2, ADD, SUB))
	{
		unaryArithmeticExpression();
		fprintf(stdout, "%s\n", ARITHMETIC_EXPRESSION);
		return;
	}

	if (comparator(lookahead.code, 4, AVID_T, FPL_T, INL_T, LPR_T))
	{
		additiveArithmeticExpression();
		fprintf(stdout, "%s\n", ARITHMETIC_EXPRESSION);
		return;
	}

	printError();
}

void stringExpression(void)
{
	primaryStringExpression();
	stringExpressionPrime();
	fprintf(stdout, "%s\n", STRING_EXPRESSION);
}

void preCondition(void)
{
	if (lookahead.code == KW_T && comparator(lookahead.attribute.get_int, 2, TRUE, FALSE))
	{
		matchToken(KW_T, lookahead.attribute.get_int);
		return;
	}

	printError();
}

void conditionalExpression(void)
{
	logicalOrExpression();

	fprintf(stdout, "%s\n", CONDITIONAL_EXPRESSION);
}

void variableList(void)
{
	variableIdentifier();
	variableListPrime();

	fprintf(stdout, "%s\n", VARIABLE_LIST);
}

void variableIdentifier(void)
{
	if (comparator(lookahead.code, 2, AVID_T, SVID_T))
	{
		matchToken(lookahead.code, NO_ATTR);
		return;
	}

	printError();
}

void variableListPrime(void)
{
	if (lookahead.code == COM_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		variableIdentifier();
		variableListPrime();
	}
}

void outputList(void)
{
	if (lookahead.code == STR_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		fprintf(stdout, "%s\n", OUTPUT_LIST);
		return;
	}

	optionalVariableList();
}

void optionalVariableList(void)
{
	if (comparator(lookahead.code, 2, AVID_T, SVID_T))
	{
		variableList();
		return;
	}

	fprintf(stdout, "%s\n", OPT_VARIABLE_LIST);
}

void unaryArithmeticExpression(void)
{
	if (lookahead.code == ART_OP_T && comparator(lookahead.attribute.arr_op, 2, ADD, SUB))
	{
		matchToken(lookahead.code, lookahead.attribute.arr_op);
		primaryArithmeticExpression();
		fprintf(stdout, "%s\n", UNARY_ARITHMETIC_EXPRESSION);
		return;
	}

	printError();
}

void additiveArithmeticExpression(void)
{
	multiplicativeArithmeticExpression();
	additiveArithmeticExpressionPrime();
}

void primaryArithmeticExpression(void)
{
	if (comparator(lookahead.code, 3, AVID_T, FPL_T, INL_T))
	{
		matchToken(lookahead.code, NO_ATTR);
		fprintf(stdout, "%s\n", PRIMARY_ARITHMETIC_EXPRESSION);
		return;
	}

	if (lookahead.code == LPR_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		arithmeticExpression();
		matchToken(RPR_T, NO_ATTR);
		fprintf(stdout, "%s\n", PRIMARY_ARITHMETIC_EXPRESSION);
		return;
	}

	printError();
}

void multiplicativeArithmeticExpression(void)
{
	primaryArithmeticExpression();
	multiplicativeArithmeticExpressionPrime();
}

void additiveArithmeticExpressionPrime(void)
{
	if (lookahead.code == ART_OP_T && comparator(lookahead.attribute.arr_op, 2, ADD, SUB))
	{
		matchToken(lookahead.code, lookahead.attribute.arr_op);
		multiplicativeArithmeticExpression();
		additiveArithmeticExpressionPrime();
		fprintf(stdout, "%s\n", ADDITIVE_ARITHMETIC_EXPRESSION);
	}
}

void multiplicativeArithmeticExpressionPrime(void)
{
	if (lookahead.code == ART_OP_T
		&& comparator(lookahead.attribute.arr_op, 2, MUL, DIV))
	{
		matchToken(lookahead.code, lookahead.attribute.arr_op);
		primaryArithmeticExpression();
		multiplicativeArithmeticExpressionPrime();
		fprintf(stdout, "%s\n", MULTIPLICATIVE_ARITHMETIC_EXPRESSION);
	}
}

void primaryStringExpression(void)
{
	if (comparator(lookahead.code, 2, SVID_T, STR_T))
	{
		matchToken(lookahead.code, NO_ATTR);
		fprintf(stdout, "%s\n", PRIMARY_STRING_EXPRESSION);
		return;
	}

	printError();
}

void stringExpressionPrime(void)
{
	if (lookahead.code == SCC_OP_T)
	{
		matchToken(lookahead.code, NO_ATTR);
		primaryStringExpression();
		stringExpressionPrime();
	}
}

void logicalOrExpression(void)
{
	logicalAndExpression();
	logicalOrExpressionPrime();
}

void logicalAndExpression(void)
{
	logicalNotExpression();
	logicalAndExpressionPrime();
}

void logicalOrExpressionPrime(void)
{
	if (lookahead.code == LOG_OP_T && lookahead.attribute.log_op == OR)
	{
		matchToken(lookahead.code, lookahead.attribute.log_op);
		logicalAndExpression();
		logicalOrExpressionPrime();
		fprintf(stdout, "%s\n", LOGICAL_OR_EXPRESSION);
	}
}

void logicalNotExpression(void)
{
	if (lookahead.code == LOG_OP_T && lookahead.attribute.log_op == NOT)
	{
		matchToken(lookahead.code, lookahead.attribute.log_op);
		fprintf(stdout, "%s\n", LOGICAL_NOT_EXPRESSION);
	}

	relationalExpression();
	fprintf(stdout, "%s\n", RELATIONAL_EXPRESSION);
}

void logicalAndExpressionPrime(void)
{
	if (lookahead.code == LOG_OP_T && lookahead.attribute.log_op == AND)
	{
		matchToken(lookahead.code, lookahead.attribute.log_op);
		logicalNotExpression();
		logicalAndExpressionPrime();
		fprintf(stdout, "%s\n", LOGICAL_AND_EXPRESSION);
	}
}

void relationalExpression(void)
{
	if (comparator(lookahead.code, 3, AVID_T, FPL_T, INL_T))
	{
		primaryARelationalExpression();
		primaryARelationalExpressionPrime();
		return;
	}

	if (comparator(lookahead.code, 2, SVID_T, STR_T))
	{
		primarySRelationalExpression();
		primarySRelationalExpressionPrime();
		return;
	}

	printError();
}

void primaryARelationalExpression(void)
{
	if (comparator(lookahead.code, 3, AVID_T, FPL_T, INL_T))
	{
		matchToken(lookahead.code, NO_ATTR);
	}
	else
	{
		printError();
	}

	fprintf(stdout, "%s\n", PRIMARY_A_RELATIONAL_EXPRESSION);
}

void primaryARelationalExpressionPrime(void)
{
	if (lookahead.code == REL_OP_T)
	{
		matchToken(lookahead.code, lookahead.attribute.log_op);
		primaryARelationalExpression();
		return;
	}

	printError();
}

void primarySRelationalExpression(void)
{
	primaryStringExpression();
	fprintf(stdout, "%s\n", PRIMARY_S_RELATIONAL_EXPRESSION);
}

void primarySRelationalExpressionPrime(void)
{
	if (lookahead.code == REL_OP_T)
	{
		matchToken(lookahead.code, lookahead.attribute.log_op);
		primarySRelationalExpression();
		return;
	}

	printError();
}
