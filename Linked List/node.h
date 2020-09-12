#ifndef node_h
#define node_h

#include <stdio.h>

/* Add employee struct and Node struct */
typedef struct employee {
    char *firstName;
    char *lastName;
} employee_t;

typedef struct node {
    employee_t * val;
    struct node * next;
} node_t;

/* Prints all employee names in the following format: lastName, firstName */
void print_list (node_t * head);

/* Adds a new node to the end of the list. */
void addToEnd(node_t **head, employee_t *employee);

/* Adds a new node to the beginning of thr list */
void addToStart(node_t ** head, employee_t *employee);

/* Remove the first node in the list. */
void removeFromStart(node_t ** head);

/* Removes the last node in the list. */
void removeLast(node_t **head);

/* Removes a node by index. */
void remove_by_index(node_t ** head, int n);

#endif /* node_h */