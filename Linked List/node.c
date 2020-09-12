#include "node.h"

/* Prints all employee names in the following format: lastName, firstName */
void print_list (node_t * head){

    if (head == NULL) 
    { 
       printf("Nothing to print\n");
       return; 
    } 

    node_t * current = head; 
    printf("\n");
    while (current != NULL){
        printf("%s %s\n", current->val->firstName, current->val->lastName);
        current = current->next;
    }
    free(current);
}

/* Adds a new node to the end of the list. */
void addToEnd(node_t **head, employee_t *employee)
{
    node_t * newNode = (node_t*)malloc(sizeof(node_t));
    node_t * current = *head;

    newNode->val = employee;
    newNode->next = NULL;

    /* If nothing has been added yet. */
    if (*head == NULL) 
    { 
       *head = newNode;
       return; 
    } 
    
    while (current->next != NULL) {
        current = current->next;
    }

    current->next = newNode;
}

/* Adds a new node to the beginning of the list. */
void addToStart(node_t ** head, employee_t *employee)
{
    node_t * newHead = (node_t *) malloc(sizeof(node_t));

    newHead->val = employee;
    newHead->next = *head;

    *head = newHead;
}

/* Remove the first node in the list. */
void removeFromStart(node_t ** head)
{
    node_t * nodeToDelete = *head;

    if ((*head)->next != NULL) {
        *head = (*head)->next;
    }
    
    free(nodeToDelete);
}

/* Removes the last node in the list. */
void removeLast(node_t **head)
{
    /* If nothing has been added yet. */
    if (*head == NULL) {
        printf("Nothing to delete\n");
        return;
    }
    else if ((*head)->next == NULL)
    {
        *head = NULL;
        return;
    }
    else
    {
        node_t * secondLastNode = *head;

        /* Loop until currentNode has no next. */
        while (secondLastNode->next->next != NULL)
        {
            secondLastNode = secondLastNode->next;
        }

        free(secondLastNode->next);
        secondLastNode->next = NULL;
    }
}

/* Removes a node by index. */
void remove_by_index(node_t ** head, int n)
{
    int i;

    if (*head == NULL) {
        printf("Nothing to delete\n");
        return;
    }
    else 
    {
        /* If index is head. */
        if (n == 0) 
        { 
            removeFromStart(head);
            return;
        }

        node_t * currentNode = *head;

        /* Find node before node at index. */
        for (i = 0; i < n - 1; i++)
        {
            if (currentNode == NULL)
            {
                printf("Index doesn't exist in list\n");
                return;
            }
            currentNode = currentNode->next;
        }

        node_t * nodeBeforeIndex = currentNode;
        node_t * nodeAtIndex = currentNode->next;
        node_t * nodeAfterIndex = currentNode->next->next;

        free(nodeAtIndex);

        nodeBeforeIndex->next = nodeAfterIndex;
    }
}