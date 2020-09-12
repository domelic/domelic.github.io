#include "helper.h"
#include "func.h"

int main(int argc, char *argv[]) {

  /* Initalize employee array and set IDs */
  int empSize = numberOfEmployees();
  int *empIdentifiers;
  initEmployeeIDs(&empIdentifiers, empSize);

  putchar('\n');

  /* Initalize department array and set CODES */
  int depSize = numberOfDepartments();
  char **depCodes;
  initDepartmentCodes(&depCodes, depSize);

  putchar('\n');

  /*
    Initalize association array and set values to employeeIndex,
    departmentIndex, -1 (for unset value).
  */
  int association[empSize*depSize][3];
  initAssociation((int*)association, empSize, depSize);
  
  /* While user hasn't chosen to quit loop through the menu */
  int menuOption, isQuit = 0;
  int employeeID, departmentCode;
  while (!isQuit) {
    printMenu();
    menuOption = menuOptions();
    if (!menuOption) {
      putchar('\n');
      continue;
    }
    /*
      Add / Delete employee 
      Prompts for employee and department ID / CODE
    */
    if (menuOption == 1 || menuOption == 2) {
      fprintf(stdout, "%s", "Employee IDs: ");
      printIntArray((int*)empIdentifiers, empSize);
      fprintf(stdout, "%s", "Department codes: ");
      printStringArray(depCodes, depSize);
      employeeID = enterEmployeeID(empIdentifiers, empSize, 5,
        "Please enter employee ID: ");
      if (employeeID == -1) {
        putchar('\n');
        continue;
      }
      departmentCode = enterDepartmentCode(depCodes, depSize, 7,
        "Please enter department code: ");
      if (departmentCode == -1) {
        putchar('\n');
        continue;
      }
    }

    /* Switch through all menu options */
    switch (menuOption) {
      /* Add */
      case 1:
        if (association[employeeID*depSize+departmentCode][2] == 1) {
          fprintf(stderr, "%s", "Employee was already in that department.\n");
        } else {
          association[employeeID*depSize+departmentCode][2] = 1;
          fprintf(stdout, "Employee successfully added to department.\n");
        }
        putchar('\n');
        break;
      /* Delete */
      case 2:
        if (association[employeeID*depSize+departmentCode][2] == -1 || association[employeeID*depSize+departmentCode][2] == 0) {
          fprintf(stderr, "%s", "Employee was not in this department.\n");
        } else {
          association[employeeID*depSize+departmentCode][2] = 0;
          fprintf(stdout, "Employee successfully removed from department.\n");
        }
        putchar('\n');
        break;
        break;
      /* Show Table */
      case 3:
	  	  fprintf(stdout, "%s", "\nEmployees\n");
        printIntArray(empIdentifiers, empSize);
		    fprintf(stdout, "%s", "\nDepartments\n");
        printStringArray(depCodes, depSize);
		    fprintf(stdout, "%s", "\nAssociation Table\n[empIndex, depIndex, value]\n");
        print2DArray((int*)association, empSize*depSize, 3);
        putchar('\n');
        break;
      /* Quit */
      case 4:
        isQuit = 1;
        break;
      default:
        break;
    }
  }
  return 0;
}