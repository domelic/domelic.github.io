/*
* File name: buffer.c
* Compiler: gcc
* Author: Damir Omelic, SID 040 918 352
* Course: CST8152 - Compilers, Lab Section: 013
* Assignment: Lab 2 / Assignment 1
* Date: 2020/09/20
* Professor: Haider Miraj
*
* Purpose: Buffer file used to allocate space for a buffer entity and a character pointer.
*          Used to read a file and store its contents in the buffer. The rules for storing
*          are following capacity and increment guides controlled by the operational mode,
*          those being fixed, additive or multiplicative. Finally, buffer content will be
*          printed to the console.
*
* Function list: bufferCreate, bufferAddChar, bufferClear, bufferFree, bufferIsFull,
*         bufferGetAddCPosition, bufferGetCapacity, bufferGetOpMode, bufferGetIncrement,
*         bufferLoad, bufferIsEmpty, bufferGetChar, bufferPrint, bufferSetEnd, bufferRetract,
*         bufferReset, bufferGetCPosition, bufferRewind, bufferGetString, bufferSetMarkPosition,
*         bufferGetFlags, bufferGetEobFlag
*/
#define _CRT_SECURE_NO_WARNINGS

#include "buffer.h"

/*
* Function name: bufferCreate()
* Purpose: Creates a new buffer on the heap and initializes default values. Sets the capacity and
*          increment values from the arguments following buffer creation rules.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: sizeof(), malloc(), free()
* Parameters: initCapacity - initial capacity of the buffer
*             incFactor - increment factor of the buffer
*             opMode - operation mode of the buffer
* Return value: address to the buffer entity structure, or NULL if invalid
* Algorithm: Creates a new buffer from the arguments. First allocates the memory for the buffer and
*            initialized the default values. For a capacity of zero, allocates space for the character
*            pointer to a default value. Otherwise, allocates the space for the given capacity.
*            Initalizes the buffer fields from the given increment factors and operational modes,
*            and sets the default flags.
*/
pBuffer bufferCreate(short initCapacity, unsigned char incFactor, char opMode)
{
    pBuffer buffer; /* declare a new buffer */
    buffer = (Buffer*)calloc(1, sizeof(Buffer)); /* allocate memory for one buffer structure */

    if (!buffer) /* terminate if memory for buffer could not be allocated */
    {
        return NULL;
    }

    /* initialize buffer to default values */
    bufferClear(buffer);

    if (initCapacity == 0) /* no initial capacity is set */
    { /* allocate memory for buffer string with default capacity */
        buffer->string = (char*)malloc(DEFAULT_INIT_CAPACITY * sizeof(char));
        if (!buffer->string)
        { /* terminate if memory could be allocated */
            free(buffer);
            return NULL;
        }
        switch (opMode)
        { /* sets the increment value based on op mode */
        case 'a': /* additive mode */
            buffer->increment = DEFAULT_INC_FACTOR;
            break;
        case 'm': /* multiplicative mode */
            buffer->increment = DEFAULT_INC_FACTOR;
            break;
        case 'f': /* fixed mode */
            buffer->increment = 0;
            break;
        default: /* terminate if invalid op mode used */
            free(buffer);
            return NULL;
            break;
        }
        buffer->capacity = DEFAULT_INIT_CAPACITY;
        buffer->increment = incFactor;
    }
    else /* initial capacity is set and is not equal to zero */
    {
        if (initCapacity < 0 || initCapacity > SHRT_MAX - 1)
        { /* terminate if outside defined range */
            free(buffer);
            return NULL;
        }
        else
        { /* allocate memory for buffer string with set capacity */
            buffer->string = (char*)malloc(initCapacity * sizeof(char));
            if (!buffer->string)
            { /* terminate if memory could not be allocated */
                free(buffer);
                return NULL;
            }
        }
        buffer->capacity = initCapacity; /* copies initial capacity to buffer capacity */
    }

    switch (opMode)
    { /* sets mode and increment value */
    case 'f': /* fixed mode */
        buffer->opMode = 0;
        buffer->increment = 0;
        break;
    case 'a': /* additive mode */
        if (incFactor != 0)
        {
            if (incFactor >= 1 && incFactor <= 255)
            { /* between 1...255 range for unsigned char */
                buffer->opMode = 1;
                buffer->increment = incFactor;
            }
            else
            { /* terminate if outside range of unsigned char */
                free(buffer);
                return NULL;
            }
        }
        break;
    case 'm':
        if (incFactor != 0)
        {
            if (incFactor >= 1 && incFactor <= 100)
            { /* between 1...100 range */
                buffer->opMode = -1;
                buffer->increment = incFactor;
            }
            else
            { /* terminate if outside range */
                free(buffer);
                return NULL;
            }
        }
        break;
    default: /* terminate of invalid mode used */
        free(buffer);
        return NULL;
        break;
    }

    if (incFactor == 0 && initCapacity != 0)
    { /* sets to fixed mode for increment factor of zero */
        buffer->opMode = 0;
        buffer->increment = 0;
    }

    buffer->flags = buffer->flags | DEFAULT_FLAGS; /* sets flags to default - 0xFFFC */

    return buffer;
}

/*
* Function name: bufferAddChar()
* Purpose: Adds a character to the buffer while respecting capacity and mode rules. Supports
*          three modes: fixed, additive, and multiplicative. Capacity remains unchanged for fixed
*          mode, and changes for additive and multiplicative mode (using formula).
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: bufferIsFull(), realloc()
* Parameters: buffer - reference to the buffer entity structure,
*             symbol - value representing the next character to be added
* Return value: reference to the buffer entity
* Algorithm: Resets the reallocation flag on each call. If the buffer is full, appropriately increases the
             capacity in relation to the increment factor for additive and multiplicative modes, or disallows
             any changes for fixed mode. In either case, reallocates space on the buffer and configures the
             buffer fields with the newly set values. Sets the reallocation flag and adds the new character.
*/
pBuffer bufferAddChar(pBuffer const buffer, char symbol)
{
    short availableSpace = 0;
    short newIncrement = 0;
    short newCapacity = 0;
    short successFlag = 0;
    char* temp_string;

    if (!buffer)
    { /* terminate if memory not allocated onto buffer */
        return NULL;
    }

    buffer->flags = buffer->flags & RESET_R_FLAG; /* reset relocate flag to 0 */

    switch (bufferIsFull(buffer))
    {
    case -1: /* run-time error */
        return NULL;
        break;
    case 0: /* buffer is not full, add that character */
        *(buffer->string + buffer->addCPosition++) = symbol;
        return buffer;
        break;
    case 1: /* buffer is full, check operational mode */
        switch (buffer->opMode)
        {
        case 0: /* fixed mode */
            return NULL;
            break;
        case 1: /* additive mode */
            newCapacity = buffer->capacity + (unsigned char)buffer->increment;
            if (newCapacity > 0)
            {
                if (newCapacity > SHRT_MAX - 1)
                {
                    newCapacity = SHRT_MAX - 1;
                }
            }
            else
            {
                return NULL;
            }
            successFlag = 1;
            break;
        case -1: /* multiplicative mode */
            if (buffer->capacity > SHRT_MAX - 1)
            {
                return NULL;
            }
            availableSpace = (SHRT_MAX - 1) - buffer->capacity;
            newIncrement = (short)(availableSpace * (double)(buffer->increment / 100.0));
            if (buffer->capacity + newIncrement > SHRT_MAX - 1)
            {
                newCapacity = SHRT_MAX - 1;
            }
            else
            {
                newCapacity = buffer->capacity + newIncrement;
            }
            successFlag = 1;
            break;
        default:
            return NULL;
            break;
        }
        break;
    default:
        return NULL;
        break;
    }

    if (successFlag)
    {
        temp_string = (char*)realloc(buffer->string, (unsigned short) newCapacity);
        if (!temp_string)
        {
            return NULL;
        }
        if (temp_string != buffer->string)
        {
            buffer->flags |= SET_R_FLAG;
			buffer->string = temp_string;
        }
        buffer->capacity = newCapacity;
        *(buffer->string + buffer->addCPosition) = symbol;
        if (buffer->addCPosition < buffer->capacity)
        {
            ++buffer->addCPosition;
        }
    }

    return buffer;
}

/*
* Function name: bufferIsFull()
* Purpose: Checks for full buffer capacity.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to buffer entity structure
* Return value: 0 if capacity limit not reached, 1 otherwise
* Algorithm: Tests if the buffer is full by comparing the value of the next position in sequence
             to be added with the buffer capacity.
*/
int bufferIsFull(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    return buffer->addCPosition == buffer->capacity;
}

/*
* Function name: bufferLoad()
* Purpose: Reads characters from the file and adds them to the buffer until EOF.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: feof(), bufferAddChar()
* Parameters: fi - file reader that has been opened with fopen()
              buffer - reference to buffer entity structure
* Return value: number of characters that have been read from the file
* Algorithm: Reads character one at a time from a file until EOF.
*/
int bufferLoad(FILE* const fi, Buffer* const buffer)
{
    char reader;
    int count = 0;

    if (!buffer)
    {
        return RT_FAIL_1;
    }

    if (!fi)
    {
        return RT_FAIL_1;
    }

    while (!feof(fi))
    {
        reader = (char)fgetc(fi);
        if (feof(fi))
        {
            break;
        }
        if (!bufferAddChar(buffer, reader))
        {
            ungetc(reader, fi);
            return LOAD_FAIL;
        }
        ++count;
    }

    return count;
}

/*
* Function name: bufferPrint()
* Purpose: Prints the contents of the buffer until reaching EOF.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: bufferGetChar(), bufferGetEobFlag()
* Parameters: buffer - reference to buffer entity reference
*             newLine - 0 or 1 if inserting new line
* Return value: number of printed characters
* Algorithm: First tests for EOB. Gets the next character in sequence from the buffer and prints it.
*/
int bufferPrint(Buffer* const buffer, char newLine)
{
    char reader;
    int count = 0;

    if (!buffer)
    {
        return LOAD_FAIL;
    }

	reader = bufferGetChar(buffer);
    while (!bufferGetEobFlag(buffer))
    {
        printf("%c", reader);
        reader = bufferGetChar(buffer);
        ++count;
    }

    if (newLine)
    {
        putchar('\n');
    }

    return count;
}

/*
* Function name: bufferGetChar()
* Purpose: Gets a character at the current read position in the buffer. If reaching end of buffer,
           set the EOB flag and return 0.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: fprint()
* Parameters: buffer - reference to buffer entity structure
* Return value: character at position for non-full buffers, otherwise 0
* Algorithm: If buffer capacity is exhausted, sets the end of buffer flag. Otherwise, resets the EOB flag.
*            Returns the next character in sequence and increments the position variable used for retrieval.
*/
char bufferGetChar(Buffer* const buffer)
{
    if (!buffer)
    {
        return LOAD_FAIL;
    }

    if (buffer->getCPosition == buffer->addCPosition)
    {
        buffer->flags &= RESET_EOB;
        buffer->flags |= SET_EOB;
        return 0;
    }
    else
    {
        buffer->flags &= RESET_EOB;
    }

    return *(buffer->string + buffer->getCPosition++);
}

/*
* Function name: bufferGetEobFlag()
* Purpose: Gets bit value of EOB field from flags variable.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions:
* Parameters: buffer - reference to buffer entity structure
* Return value: bit value of EOB field
* Algorithm: Returns the bit value of the EOB field of the flags variable.
*/
int bufferGetEobFlag(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    return buffer->flags & CHECK_EOB;
}

/*
* Function name: bufferGetCapacity()
* Purpose: Getter for the buffer capacity.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: value representing the capacity; -1 on blank buffer
* Algorithm: Returns the capacity of the buffer entity.
*/
short bufferGetCapacity(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    return buffer->capacity;
}

/*
* Function name: bufferGetAddCPosition()
* Purpose: Getter for the buffer addCPosition value.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: value representing the addCPosition; -1 on blank buffer
* Algorithm: Returns the addCPosition of the buffer entity.
*/
short bufferGetAddCPosition(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    return buffer->addCPosition;
}

/*
* Function name: bufferGetOpMode()
* Purpose: Getter for the buffer opMode.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: reference to the buffer entity structure
* Return value: value representing the buffer opMode; -1 on blank buffer
* Algorithm: Returns the opMode of the buffer entity.
*/
int bufferGetOpMode(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    return buffer->opMode;
}

/*
* Function name: bufferGetIncrement()
* Purpose: Getter for the increment value.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: value representing the increment; 0x100 for invalid values; -1 on blank buffer
* Algorithm: Returns a special value set at 0x100 if the increment value is invalid. Otherwise,
             returns the increment value cast to size_t.
*/
size_t bufferGetIncrement(Buffer* const buffer)
{
    if (!buffer)
    {
        return (size_t) RT_FAIL_1;
    }
    if (buffer->increment < 0)
    {
        return 0x100;
    }
    return (unsigned char)buffer->increment;
}

/*
* Function name: bufferGetString()
* Purpose: Returns the pointer to a location in the character buffer.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure,
*             charPosition - value where the pointer is being moved
* Return value: Reference of the character buffer position.
* Algorithm: Checks if the position is invalid. Returns a reference to the position
*            the character reference is pointing to.
*/
char* bufferGetString(Buffer* const buffer, short charPosition)
{
    if (!buffer)
    {
        return NULL;
    }

    if (charPosition > buffer->addCPosition)
    {
        return NULL;
    }

    return buffer->string + charPosition;
}

/*
* Function name: bufferFree()
* Purpose: Frees the memory associated with the buffer entity.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: No return value
* Algorithm: First deallocate the string and then the buffer entity.
*/
void bufferFree(Buffer* const buffer)
{
    if (!buffer)
    {
        return;
    }
    
    free(buffer->string);
    free(buffer);

    return;
}

/*
* Function name: bufferSetEnd()
* Purpose: Expands the buffer to a new capacity.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: realloc() 
* Parameters: buffer - reference to the buffer entity structure,
*             symbol - character to add to the end of the buffer
* Return value: reference to the buffer; NULL on runtime errors
* Algorithm: Reallocates the memory reserved for the string on the buffer by increasing the
             capacity by 1. If successful, redeclare the buffer fields for the string and capacity,
             and add a character to the end of the buffer. Sets the reallocation flag and checks
             for the end of buffer.
*/
Buffer* bufferSetEnd(Buffer* const buffer, char symbol)
{
    short newCapacity;
    char* tempString;

    if (!buffer)
    {
        return NULL;
    }

    buffer->flags &= RESET_R_FLAG;
    newCapacity = buffer->addCPosition + 1;

    if (newCapacity <= 0)
    {
      return NULL;
    }

    tempString = (char*) realloc(buffer->string, sizeof(char) * newCapacity);

    if (!tempString)
    {
      return NULL;
    }

    if (tempString != buffer->string)
    {
		buffer->flags |= SET_R_FLAG;
		buffer->string = tempString;
    }

    buffer->capacity = newCapacity;
    *(buffer->string + buffer->addCPosition++) = symbol;
    
    return buffer;
}

/*
* Function name: bufferGetFlags()
* Purpose: Getter for the flags variable.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: value representing the flags; -2 on blank buffer
* Algorithm: Returns the flags variable.
*/
unsigned short bufferGetFlags(pBuffer const buffer)
{
    if (!buffer)
    {
        return (unsigned short) RT_FAIL_2;
    }

    return buffer->flags;
}

/*
* Function name: bufferRewind()
* Purpose: Enables rereading the buffer by reinitalizing getCPosition and markCPosition.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: 0 if positions reinitialized; -1 on blank buffer
* Algorithm: Sets getCPosition and markCPosition to zero and returns.
*/
int bufferRewind(Buffer* const buffer)
{
    if (!buffer)
    {
        return RT_FAIL_1;
    }

    buffer->getCPosition = 0;
    buffer->markCPosition = 0;

    return 0;
}

/*
* Function name: bufferClear()
* Purpose: Initialize the buffer fields to default values.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: 0 if buffer reinitialized; -1 on blank buffer
* Algorithm: Sets buffer fields to default values and returns.
*/
int bufferClear(Buffer* const buffer)
{
  if (!buffer)
  {
      return RT_FAIL_1;
  }

  buffer->addCPosition = 0;
  buffer->getCPosition = 0;
  buffer->markCPosition = 0;
  buffer->flags = 0;

  return 0;
}

/*
* Function name: bufferIsEmpty()
* Purpose: Checks if the buffer is empty by comparing addCPosition to 0.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: 1 if buffer is empty, 0 otherwise; -1 on blank buffer 
* Algorithm: Returns the comparison of addCPosition to 0.
*/
int bufferIsEmpty(Buffer* const buffer)
{
  if (!buffer)
  {
      return RT_FAIL_1;
  }

  return (buffer->addCPosition == 0);
}

/*
* Function name: bufferRetract()
* Purpose: Decrements the value of getCPosition.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure
* Return value: value representing the getCPosition decremented by 1; -1 on blank buffer
* Algorithm: Decrements the value of getCPosition by 1 and returns.
*/
short bufferRetract(Buffer* const buffer)
{
  if (!buffer)
  {
      return RT_FAIL_1;
  }

  return (--buffer->getCPosition);
}

/*
* Function name: bufferReset()
* Purpose: Sets the value of getCPosition to markCPosition.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: N/A
* Parameters:  buffer - reference to the buffer entity structure
* Return value: value representing the getCPosition from the buffer; -1 on blank buffer
* Algorithm: Assigns the value of markCPosition to getCPosition and returns that value.
*/
short bufferReset(Buffer* const buffer)
{
  if (!buffer)
  {
      return RT_FAIL_1;
  }

  buffer->getCPosition = buffer->markCPosition;

  return buffer->getCPosition;
}

/*
* Function name: bufferGetCPosition()
* Purpose: Getter for GetCPosition.
* Author: Damir Omelic
* History: 1.0, 2020/09/20
* Called functions: N/A
* Parameters:  buffer - reference to the buffer entity structure
* Return value: value representing the character position; NULL on blank buffer
* Algorithm: Returns the getCPosition from the buffer.
*/
short bufferGetCPosition(Buffer* const buffer)
{
  if (!buffer)
  {
      return RT_FAIL_1;
  }

  return buffer->getCPosition;
}

/*
* Function name: bufferSetMarkPosition()
* Purpose: Setter for markCPosition.
* Author: Damir Omelic
* History: 1.0, 2020/10/03
* Called functions: N/A
* Parameters: buffer - reference to the buffer entity structure,
*             mark   - position to set
* Return value: reference to the buffer entity, NULL on blank buffer
* Algorithm: Sets markCPosition from the argument within capacity.
*/
pBuffer bufferSetMarkPosition(pBuffer const buffer, short mark)
{
  if (!buffer)
  {
      return NULL;
  }
  if (mark >= 0 && mark <= buffer->addCPosition)
  {
	buffer->markCPosition = mark;
	  return buffer; 
  }

  return NULL;
}
