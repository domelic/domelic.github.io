/*
* File name: buffer.h
* Compiler: gcc
* Author: Damir Omelic, SID 040 918 352
* Course: CST8152 - Compilers, Lab Section: 013
* Assignment: Lab 2 / Assignment 1
* Date: 2020/09/20
* Professor: Haider Miraj
*
* Purpose: Header file for the buffer program, containingp preprocessor directives, type declarations
*          and prototype functions necessary for the program implementation.
*
* Function list: bufferCreate, bufferAddChar, bufferClear, bufferFree, bufferIsFull,
*         bufferGetAddCPosition, bufferGetCapacity, bufferGetOpMode, bufferGetIncrement,
*         bufferLoad, bufferIsEmpty, bufferGetChar, bufferPrint, bufferSetEnd, bufferRetract,
*         bufferReset, bufferGetCPosition, bufferRewind, bufferGetString, bufferSetMarkPosition,
*         bufferGetFlags, bufferGetEobFlag
*/

#ifndef BUFFER_H_
#define BUFFER_H_

/*#pragma warning(1:4001) *//*to enforce C89 type comments  - to make //comments an warning */

/*#pragma warning(error:4001)*//* to enforce C89 comments - to make // comments an error */

/*************************/
/* STANDARD HEADER FILES */
/*************************/
#include <stdio.h>  /* standard input/output */
#include <malloc.h> /* for dynamic memory allocation*/
#include <limits.h> /* implementation-defined data type ranges and limits */

#include <stdlib.h>
#include <string.h>

/************************/
/* CONSTANT DEFINITIONS */
/************************/
#define RT_FAIL_1 (-1) /* operation failure return value 1 */
#define RT_FAIL_2 (-2) /* operation failure return value 2 */
#define LOAD_FAIL (-2) /* load fail return value */

#define DEFAULT_INIT_CAPACITY 200 /* default initial buffer capacity */
#define DEFAULT_INC_FACTOR 15     /* default increment factor */

#define MAXIMUM_ALLOWED_POSITIVE_VALUE 100

/**********************************/
/* BIT-MASKS CONSTANT DEFINITIONS */
/**********************************/
#define DEFAULT_FLAGS 0xFFFC /* 1111.1111 1111.1001 */
#define SET_EOB       0x0001 /* 0000.0000 0000.0001 */
#define RESET_EOB     0xFFFE /* 1111.1111 1111.1110 */
#define CHECK_EOB     0x0001 /* 0000.0000 0000.0001 */
#define SET_R_FLAG    0x0002 /* 0000.0000 0000.0010 */
#define RESET_R_FLAG  0xFFFD /* 1111.1111 1111.1101 */
#define CHECK_R_FLAG  0x0002 /* 0000.0000 0000.0010 */

/*******************************/
/* USER DATA TYPE DECLARATIONS */
/*******************************/
typedef struct BufferEntity {
	char* string;         /* pointer to the beginning of character array (character buffer) */
	short capacity;       /* current dynamic memory size (in bytes) allocated to character buffer */
	short addCPosition;   /* the offset (in chars) to the add-character location */
	short getCPosition;   /* the offset (in chars) to the get-character location */
	short markCPosition;  /* the offset (in chars) to the mark location */
	char  increment;      /* character array increment factor */
	char  opMode;         /* operational mode indicator*/
	unsigned short flags; /* contains character array reallocation flag and end-of-buffer flag */
} Buffer, * pBuffer;

/*************************/
/* FUNCTION DECLARATIONS */
/*************************/
pBuffer bufferCreate(short, unsigned char, char);
pBuffer bufferAddChar(pBuffer const, char);
int bufferClear(Buffer* const);
void bufferFree(Buffer* const);
int bufferIsFull(Buffer* const);
short bufferGetAddCPosition(Buffer* const);
short bufferGetCapacity(Buffer* const);
int bufferGetOpMode(Buffer* const);
size_t bufferGetIncrement(Buffer* const);
int bufferLoad(FILE* const, Buffer* const);
int bufferIsEmpty(Buffer* const);
char bufferGetChar(Buffer* const);
int bufferPrint(Buffer* const, char);
Buffer* bufferSetEnd(Buffer* const, char);
short bufferRetract(Buffer* const);
short bufferReset(Buffer* const);
short bufferGetCPosition(Buffer* const);
int bufferRewind(Buffer* const);
char* bufferGetString(Buffer* const, short);
pBuffer bufferSetMarkPosition(pBuffer const, short);
unsigned short bufferGetFlags(pBuffer const);
int bufferGetEobFlag(Buffer* const);

#endif
