/*************************************************************
 * Filename: scanner.c
 * Compiler: gcc
 * Created by: Svillen Ranev - Updated by: Paulo Sousa
 * Author: Damir Omelic, SID 040 918 352,
		   Sam Keang SID 040 736 101
 * Course: CST8152 - Compilers, Lab Section: 013, 011
 * Assignment: 2
 * Date: 2020/11/14
 * Professor: Khadidja Kaci Taouch, Haider Miraj
 *
 * Purpose: Scanner or lexical analyzer will use the buffer
 *			from assignment 1 to read a source program and
 *			create a create a token representation according
 *			to the grammar outlined in Platy 2.0.
 *
 ************************************************************/

 /* The #define _CRT_SECURE_NO_WARNINGS should be used in MS Visual Studio projects
  * to suppress the warnings about using "unsafe" functions like fopen()
  * and standard sting library functions defined in string.h.
  * The define does not have any effect in Borland compiler projects.
  */
#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>   /* standard input / output */
#include <ctype.h>   /* conversion functions */
#include <stdlib.h>  /* standard library functions and constants */
#include <string.h>  /* string functions */
#include <limits.h>  /* integer types constants */
#include <float.h>   /* floating-point types constants */

  /*#define NDEBUG        to suppress assert() call */
#include <assert.h>  /* assert() prototype */

/* project header files */
#include "buffer.h"
#include "token.h"
#include "table.h"
#include "parser.h"

#define DEBUG  /* for conditional processing */
#undef  DEBUG

/* Global objects - variables */
/* This buffer is used as a repository for string literals.
   It is defined in platy_st.c */
extern pBuffer stringLiteralTable;		/* String literal table */
int line;								/* current line number of the source code */
extern int errorNumber;					/* defined in platy_st.c - run-time error number */

static char debugMode = 0;				/* optional for debugging */

/* Local(file) global objects - variables */
static pBuffer lexemeBuffer;			/* pointer to temporary lexeme buffer */
static pBuffer sourceBuffer;			/* pointer to input source buffer */
/* No other global variable declarations/definitiond are allowed */

/* scanner.c static(local) function  prototypes */
static int nextTokenClass(char c);		/* character class function */
static int getNextState(int, char);		/* state machine function */
static int isKeyword(char* kw_lexeme);	/* keywords lookup functuion */

/*************************************************************
 *
 * Function name: initScanner()
 * Purpose: Initializes the scanner.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: bufferIsEmpty(), bufferRewind(), bufferClear()
 * Parameter: psc_buff
 * Return value: EXIT_SUCCESS, value of 0
 * Algorithm: Initializes the scanner passing in a buffer as an 
 *			  argument. If the buffer is valid set getCPosition
 *			  and markCPosition to the beginning in order to start
 *			  analyzing. The stringLiteralTable fields should
 *			  also be set to default values of zero. If successful
 *			  0 is returned.
 *
 ************************************************************/
int initScanner(pBuffer psc_buf) {
	if (bufferIsEmpty(psc_buf)) return EXIT_FAILURE;/*1*/
	/* in case the buffer has been read previously  */
	bufferRewind(psc_buf);
	bufferClear(stringLiteralTable);
	line = 1;
	sourceBuffer = psc_buf;
	return EXIT_SUCCESS;/*0*/
/*   scerrnum = 0;  *//*no need - global ANSI C */
}

/*************************************************************
 *
 * Function name: processToken()
 * Purpose: Recognize token from reading source program using
 *			the main buffer and a temporary lexemeBuffer.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: bufferGetChar(), bufferRetract(),
 *					 bufferSetMarkPosition(), bufferGetCPosition,
 *					 bufferAddChar(), getNextState(), bufferCreate,
 *					 bufferReset, bufferSetEnd(), bufferGetString(),
 *					 free()
 *
 * Parameter: none
 * Return value: currentToken
 * Algorithm: Create a token structure that will hold a code and
 *			  attribute value depending on the case. The Token 
 *			  driven processing will be used to check for  
 *			  arithmetic, relational, logical, and special symbols 
 *			  as outlined in Platy 2.0. All other case will be 
 *			  processed using the TT with accepting functions and 
 *			  the stateTypes. Once consumed a DFA can be determined.
 *
 ************************************************************/
Token processToken(void) {
	Token currentToken = { 0 }; /* token to return after pattern recognition. Set all structure members to 0 */
	unsigned char c;						/* input symbol */
	unsigned char newc;					/* new char */
	int lexLength = 0;					/* token length */
	int state = 0;							/* initial state of the FSM */
	int i = 0;									/* counter */

	while (1) 
	{ /* endless loop broken by token returns it will generate a warning */
		c = bufferGetChar(sourceBuffer);

		/*Token Driven Processing*/
		switch (c) 
		{/*sets the code and attribute value based on character symbol*/
			case ' ': case '\t': case '\v': case '\f': case '\r':/*determine type of whitespace character*/
				break;

			case '\n':/*handle if newline character*/
				++line;
				break;

	  /*Checking for arithmetic operator */
      case '+':
        currentToken.code = ART_OP_T;
        currentToken.attribute.arr_op = ADD;
        return currentToken;
        break;

      case '-':
        currentToken.code = ART_OP_T;
        currentToken.attribute.arr_op = SUB;
        return currentToken;
        break;

      case '*':
        currentToken.code = ART_OP_T;
        currentToken.attribute.arr_op = MUL;
        return currentToken;
        break;

      case '/':
        currentToken.code = ART_OP_T;
        currentToken.attribute.arr_op = DIV;
        return currentToken;
        break;

	  /*Checking for relational & assignment operator */
      case '=': 
        if (bufferGetChar(sourceBuffer) == '=')
        {/*check if relational operation*/
          currentToken.code = REL_OP_T;
          currentToken.attribute.rel_op = EQ;
          return currentToken;
        }
        bufferRetract(sourceBuffer);
        currentToken.code = ASS_OP_T;
        return currentToken;
        break;

      case '>':/*checking if greater than*/
        currentToken.code = REL_OP_T;
        currentToken.attribute.rel_op = GT;
        return currentToken;
        break;

      case '<':/*checking if less than*/
        if (bufferGetChar(sourceBuffer) == '>')/*handle if not equal, <>*/
        {
          currentToken.code = REL_OP_T;
          currentToken.attribute.rel_op = NE;
          return currentToken;
        }
		bufferRetract(sourceBuffer);
        currentToken.code = REL_OP_T;
        currentToken.attribute.rel_op = LT;
        return currentToken;
        break;
			
			/*Checking for source end of file*/
			case CHARSEOF0:/*checking if line termination*/
				currentToken.code = SEOF_T;
				currentToken.attribute.seof = SEOF_0;
				return currentToken;
				break;

			case CHARSEOF255:/*checking if end of file*/
				currentToken.code = SEOF_T;
				currentToken.attribute.seof = SEOF_EOF;
				return currentToken;
				break;

			/*Checking for opening and closing function symbols*/
			case '(':/*checking for opening parenthesis*/
				currentToken.code = LPR_T;
				return currentToken;
				break;

			case ')':/*checking for closing parenthesis*/
				currentToken.code = RPR_T;
				return currentToken;
				break;

			case '{':/*checking for opening brace*/
				currentToken.code = LBR_T;
				return currentToken;
				break;
				
			case '}':/*checking for closing brace*/
				currentToken.code = RBR_T;
				return currentToken;
				break;

			/*Checking for other symbols*/
			case ',':/*checking when comma operator*/
				currentToken.code = COM_T;
				return currentToken;
				break;

			case '$':/*check if should be an error*/
				if (bufferGetChar(sourceBuffer) == '$')/*checking if string concatenation*/
				{
					currentToken.code = SCC_OP_T;
					return currentToken;
				}
				bufferRetract(sourceBuffer);
				currentToken.code = ERR_T;
				currentToken.attribute.err_lex[0] = c;
				return currentToken;
				break;

			/*checking for logical operator */
			case '_':
				bufferSetMarkPosition(sourceBuffer, bufferGetCPosition(sourceBuffer));/*setting mark position after first _*/
				switch (bufferGetChar(sourceBuffer))
				{
					case 'A':/*checking if _AND_*/
						if (bufferGetChar(sourceBuffer) == 'N' && bufferGetChar(sourceBuffer) == 'D' &&
								bufferGetChar(sourceBuffer) == '_')
						{
							currentToken.code = LOG_OP_T;
							currentToken.attribute.log_op = AND;
							return currentToken;
						}
						break;
					case 'N':/*checking if _NOT_*/
						if (bufferGetChar(sourceBuffer) == 'O' && bufferGetChar(sourceBuffer) == 'T' &&
								bufferGetChar(sourceBuffer) == '_')
						{
							currentToken.code = LOG_OP_T;
							currentToken.attribute.log_op = NOT;
							return currentToken;
						}
						break;
					case 'O':/*checking if _OR_*/
						if (bufferGetChar(sourceBuffer) == 'R' && bufferGetChar(sourceBuffer) == '_')
						{
							currentToken.code = LOG_OP_T;
							currentToken.attribute.log_op = OR;
							return currentToken;
						}
						break;
					default:
						break;
				}/*handle for error*/
				bufferReset(sourceBuffer);
				currentToken.code = ERR_T;
				currentToken.attribute.err_lex[0] = c;
				return currentToken;
				break;
		
			case ';': /*checking if statement*/
				currentToken.code = EOS_T;
				return currentToken;
				break;

			/*Checking for comments*/
			case '!':
				/*handle immediately if not !*/
				if ((newc = bufferGetChar(sourceBuffer)) != '!')
				{
					currentToken.code = ERR_T;
					snprintf(currentToken.attribute.err_lex, 3, "%c%c%c", c, newc, '\0');
				}
				while ((newc = bufferGetChar(sourceBuffer)))
				{
					if (newc == '\n') { ++line; break; }
					else if (newc == CHARSEOF0 || newc == CHARSEOF255) { break; }
				}
				if (currentToken.code == ERR_T)
				{
					return currentToken;
				}
				else if (newc != '\n')
				{
					currentToken.code = ERR_T;
					snprintf(currentToken.attribute.err_lex, 3, "%s", "!!\0");
					return currentToken;
				}
				break;

		/*Processing with transition table*/
		default:
			bufferSetMarkPosition(sourceBuffer, bufferGetCPosition(sourceBuffer) - 1);/*mark position*/
			state = getNextState(state, c);/*set state to be the next state*/
			while (stateType[state] == NOAS)/*check if state is NOAS*/
			{
				c = bufferGetChar(sourceBuffer);
				state = getNextState(state, c);
				++lexLength;
			}
			if (stateType[state] == ASWR)/*if ASWR retract*/
			{
				bufferRetract(sourceBuffer);
			}
			else 
			{
				++lexLength;
			}
			bufferReset(sourceBuffer);
			lexemeBuffer = bufferCreate((short) lexLength, 0, 'f');/*create lexemeBuffer*/
			for (i = 0; i < lexLength; i++)
			{
				c = bufferGetChar(sourceBuffer);
				bufferAddChar(lexemeBuffer, c);
			}
			bufferSetEnd(lexemeBuffer, CHARSEOF0);/*set line terminator symbol*/
			currentToken = finalStateTable[state](bufferGetString(lexemeBuffer, 0));/*call appropriate accepting function*/
			free(lexemeBuffer);
			return currentToken;
			break;
		} 
	} 
} 

/*************************************************************
 *
 * Function name: getNextState()
 * Purpose: finds the next state from the column and current
 *			state in the TT.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: nextTokenClass(), assert()
 * Parameter: state, c
 * Return value: next
 * Algorithm: A state machine function used to find the next state
 *			  in the TT driven portion of the proccesToken(). It 
 *			  in a character value as argument so that it can find
 *			  the column using nextTokenClass(). This function
 *			  also takes state, row as an argument. With the column
 *			  and row of the TT the position should be checked
 *			  to ensure it is not an IS, if not it is returned.
 *
 ************************************************************/
int getNextState(int state, char c) {
	int col;
	int next;
	if (debugMode)
		fprintf(stderr, "[03] getNextState\n");
	col = nextTokenClass(c);
	next = transitionTable[state][col];
#ifdef DEBUG
	printf("Input symbol: %c Row: %d Column: %d Next: %d \n", c, state, col, next);
#endif
	/*
	The assert(int test) macro can be used to add run-time diagnostic to programs
	and to "defend" from producing unexpected results. assert() is a macro that
	expands to an if statement; if test evaluates to false (zero), assert aborts
	the program (by calling abort()) and sends the following message on stderr:
	Assertion failed: test, file filename, line linenum
	The filename and linenum listed in the message are the source file name and
	line number where the assert macro appears. If you place the #define NDEBUG
	directive ("no debugging") in the source code before the #include <assert.h>
	directive, the effect is to comment out the assert statement.
	*/
	assert(next != IS);

	/*
	The other way to include diagnostics in a program is to use conditional
	preprocessing as shown bellow. It allows the programmer to send more details
	describing the run-time problem. Once the program is tested thoroughly
	#define DEBUG is commented out or #undef DEBUF is used - see the top of the file.
	*/
#ifdef DEBUG
	if (next == IS) {
		printf("Scanner Error: Illegal state:\n");
		printf("Input symbol: %c Row: %d Column: %d\n", c, state, col);
		exit(1);
	}
#endif
	return next;
}

/*************************************************************
 *
 * Function name: nextTokenClass()
 * Purpose: Finds the column value in the TT.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: isalpha(), isdigit()
 * Parameter: c
 * Return value: c
 * Algorithm: Checking to find the appropriate column position
 *			  for the character it is given as an argument. 
 *
 ************************************************************/
int nextTokenClass(char c) {
	int val = -1;
	if (debugMode)
		fprintf(stderr, "[04] NextTokenClass\n");

	if (isalpha(c))/*check if character is alphabet*/
	{
		val = 0;
	}
	else if (isdigit(c))/*check if character is a numeric*/
	{
		if (c == '0')
		{
			val = 1;
		}
		else
		{
			val = 2;
		}
	}
	else
	{
		switch (c) {/*set col value for each case*/
			case '.':/*FPL*/
				val = 3;
				break;
			case '$':/*SVID*/
				val = 4;
				break;
			case '\"':/*SL*/
				val = 5;
				break;
			case CHARSEOF0: case CHARSEOF255:/*\0 or 255*/
				val = 6;
				break;
			default:/*other symbols*/
				val = 7;
				break;
		} 
	}

	return val;
}

/*************************************************************
 *
 * Function name: aStateFuncAVID()
 * Purpose: Accepting State Function, checks for VID, AVID and KW
 *			and assigns code and attribute for currentToken
 *			appropriately.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: isKeyword(), strlen(), strcpy()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: AVID_T should be checked to ensure they are not
 *			  tokens before they are set as an attribute for the
 *			  token structure. As per Platy 2.0 specifications,
 *			  VID_LEN value is the max length a lexeme can be,
 *			  therefore only the first 8 characters will be stored
 *
 ************************************************************/
Token aStateFuncAVID(char lexeme[]) {
	Token currentToken = { 0 };/* token to return after pattern recognition */
	int i = 0;
	int code;

	if ((code = isKeyword(lexeme)) != -1)/* if KW exists set code and attr */
	{
		currentToken.code = KW_T;
		currentToken.attribute.get_int = code;
		currentToken.attribute.kwt_idx = code;
		return currentToken;
	}

	currentToken.code = AVID_T;

	if (strlen(lexeme) > VID_LEN)/*compare length of lexeme to that of VID_LEN*/
	{
		for (i = 0; i < VID_LEN; ++i)/* store first 8 characters */
		{
			currentToken.attribute.vid_lex[i] = lexeme[i];/*set value for attribute*/
		}
	}
	else
	{
		strcpy(currentToken.attribute.vid_lex, lexeme);/*set value for attribute*/
	}

	currentToken.attribute.vid_lex[strlen(currentToken.attribute.vid_lex)] = CHARSEOF0;/*set \0*/
	
	return currentToken;
}

/*************************************************************
 *
 * Function name: aStateFuncSVID()
 * Purpose: Accepting State Function, checks for SVID and assigns
 *			code and attribute for currentToken appropriately.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 *		   and Sam Keang
 * History: 1.0, 2020/11/14
 * Called functions: strlen(), strcpy()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: The first characters of the lexeme up to VID_LEN-1 will
 *			  be stored as SVID requires an $ to appened. Token structure
 *			  code and attribute will be set regardless. 
 *
 ************************************************************/
Token aStateFuncSVID(char lexeme[]) {
	Token currentToken = { 0 };/* token to return after pattern recognition */
	int i;

	currentToken.code = SVID_T;

	if (strlen(lexeme) > VID_LEN)/*compare length of lexeme to that of VID_LEN*/
	{
		for(i = 0; i < VID_LEN - 1; ++i)/* store first 7 characters*/
		{
			currentToken.attribute.vid_lex[i] = lexeme[i];/*set value for attribute*/
		}
		currentToken.attribute.vid_lex[strlen(currentToken.attribute.vid_lex)] = '$';/*append to end of attribute value*/
	}
	else
	{
		strcpy(currentToken.attribute.vid_lex, lexeme);/*set value for attribute*/
	}
	currentToken.attribute.vid_lex[strlen(currentToken.attribute.vid_lex)] = CHARSEOF0;/*set \0*/
	
	return currentToken;
}

/*************************************************************
 *
 * Function name: aStateFuncIL()
 * Purpose: Accepting State Function, checks for IL and assigns
 *			code and attribute for currentToken appropriately
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: strtol(), strlen(), strcat(), strcpy()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: Converts the string value lexeme into a numerical
 *			  value, so that it is correctly translated. Token
 *			  structure will be assigned a code and attribute,
 *			  depending if there is an error or not.
 *
 ************************************************************/
Token aStateFuncIL(char lexeme[]) {
	Token currentToken = { 0 };/* token to return after pattern recognition */
	char *pEnd;
	int i;
	long int value = strtol(lexeme, &pEnd, 10);/* string to long int */
	if (value > SHRT_MAX || value < SHRT_MIN)/* if not 2 bytes */
	{
		currentToken.code = ERR_T;
		if (strlen(lexeme) > ERR_LEN)/*compare length of lexeme to that of VID_LEN*/
		{
			for (i = 0; i < ERR_LEN - 3; ++i)/* remove 3 char to make space to append ... */
			{
				currentToken.attribute.err_lex[i] = lexeme[i];/*set value for attribute*/
			}
			strcat(currentToken.attribute.err_lex, "...");
		}
		else
		{
			strcpy(currentToken.attribute.err_lex, lexeme);/*set value for attribute*/
		}
		return currentToken;
	}
	else
	{
		currentToken.code = INL_T;
		currentToken.attribute.int_value = (short) value;/*ensure value is correct byte value*/
	}
	
	return currentToken;
}

/*************************************************************
 *
 * Function name: aStateFuncFPL()
 * Purpose: Accepting State Function, checks for FPL and assigns
 *			code and attribute for currentToken appropriately.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 *			and Sam Keang
 * History: 1.0, 2020/11/14
 * Called functions: atof(), strlen(), strcat(), strcpy()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: Converts the string value lexeme into a numerical
 *			  value, so that it is correctly translated. Token
 *			  structure will be assigned a code and attribute,
 *			  depending if there is an error or not.
 *
 ************************************************************/
Token aStateFuncFPL(char lexeme[]) {
	Token currentToken = { 0 }; /* token to return after pattern recognition */
	float floatPL;
	int i;
	
	floatPL = (float) atof(lexeme); /* string to float */

	if (floatPL > FLT_MAX || floatPL < FLT_MIN)/* if not 4 bytes add error code and attr */
	{
		currentToken.code = ERR_T;
		if (strlen(lexeme) > ERR_LEN)/*compare length of lexeme to that of VID_LEN*/
		{
			for (i = 0; i < ERR_LEN - 3; ++i)/* remove 3 char to make space to append ... */
			{
				currentToken.attribute.err_lex[i] = lexeme[i];/*set value for attribute*/
			}
			strcat(currentToken.attribute.err_lex, "...");
			return currentToken;
		}
		else 
		{
			strcpy(currentToken.attribute.err_lex, lexeme);/*set value for attribute*/
		}
	}

	currentToken.code = FPL_T;
	currentToken.attribute.flt_value = (float) floatPL;/*ensure that value is correct byte size*/

	return currentToken;
}

/*************************************************************
 *
 * Function name: aStateFuncSL()
 * Purpose: Accepting State Function, checks for SL and assigns
 *			code and attribute for currentToken appropriately.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: strlen(), bufferGetAddCPosition(), bufferAddChar()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: The token code will be assigned STR_T to represent
 *			  SL. To obtain the attribute bufferAddChar() is used
 *			  as it will store each character of the lexeme in the
 *			  stringLiteralTable. The " symbol will not be counted 
 *			  as character stored as an attribute value.
 *			   
 *
 ************************************************************/
Token aStateFuncSL(char lexeme[]) {
	Token currentToken = { 0 };/* token to return after pattern recognition */
	unsigned int i;

	currentToken.code = STR_T;
	currentToken.attribute.str_offset = bufferGetAddCPosition(stringLiteralTable);

	for (i = 0; i < strlen(lexeme); i++)/* iterate through lexeme char by char */
	{
		if (lexeme[i] == '\n')
		{
			++line;
		}
		if (lexeme[i] != '\"') /* ignore " */
		{
			bufferAddChar(stringLiteralTable, lexeme[i]);/*add lexeme*/
		}
	}
	bufferAddChar(stringLiteralTable, CHARSEOF0);/*add \0*/

	return currentToken;
}

/*************************************************************
 *
 * Function name: aStateFuncErr()
 * Purpose: Accepting State Function, checks for error tokens
 *			and assigns code and attribute for currentToken
 *			appropriately.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: strlen(), strcat(), strcpy()
 * Parameter: lexeme[]
 * Return value: currentToken
 * Algorithm: In the case of an error, the token structure
 *			  must be assigned the appropriate code and attribute
 *			  so that it can be recognized.
 *
 ************************************************************/
Token aStateFuncErr(char lexeme[]) {
	Token currentToken = { 0 };/* token to return after pattern recognition */
	unsigned int i;

	currentToken.code = ERR_T;
	if (strlen(lexeme) > ERR_LEN)
	{
		for (i = 0; i < ERR_LEN - 3; ++i) /* remove 3 char to make space to append ... */
		{
			currentToken.attribute.err_lex[i] = lexeme[i];/*set value for attribute*/
		}
		strcat(currentToken.attribute.err_lex, "...");
	}
	else
	{
		strcpy(currentToken.attribute.err_lex, lexeme);/*set value for attribute*/
	}

	for (i = 0; i < strlen(lexeme); ++i)
	{
		if (lexeme[i] == '\n') { ++line; }
	}

	return currentToken;
}

/*************************************************************
 *
 * Function name: isKeyword()
 * Purpose: Checks if lexeme is a KW.
 * Author: Svillen Ranev - Paulo Sousa modified by Damir Omelic
 * History: 1.0, 2020/11/14
 * Called functions: strcmp()
 * Parameter: kw_lexeme[]
 * Return value: RT_FAIL_1, value of -1
 * Algorithm: To ensure keywords are recognized, lexeme passed in
 *			  as an argument is compared with values in the 
 *			  keywordTable.
 *
 ************************************************************/
int isKeyword(char* kw_lexeme) {
	int i;
	for (i = 0; i < KWT_SIZE; ++i)/*size as defined in table.h*/
	{
		if (strcmp(kw_lexeme, keywordTable[i]) == 0) /* compare lexeme with each keyword value*/
		{
			return i;/*index found*/
		}
	}
	return RT_FAIL_1;
}