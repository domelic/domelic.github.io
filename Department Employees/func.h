#ifndef func_h
#define func_h

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

char * numberPrompt(char*);
int numberOfEmployees();
int isValidNumber(char*);
int countDigits(char*);
int getNumericValue(int, int, char*);
int convertToInt(char*, int);
int intArrayIsUnique(int[], int);
void initEmployeeIDs(int **, int);
int numberOfDepartments();
void initDepartmentCodes(char ***, int);
char * getDepartmentCode(int, char*);
int isValidCode(char*);
void printMenu();
int menuOptions();
int enterEmployeeID(int*, int, int, char*);
int enterDepartmentCode(char**, int, int, char*);
void initAssociation(int *, int, int);

#endif