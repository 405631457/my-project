#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <windows.h>
#define MAX 10

typedef struct Name{
	char *player1;
	char *player2;
}Name;

typedef struct Final{
	Name *userName;
	char *winner;
	int round;
	struct Final *next;
}Final;

typedef Final * fiPtr;
Final *head=NULL;
Final *tail=NULL;
 
void clean();
/**
* clean the array which wii be stored from user's input.
*
* @param make all block became null.
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.
*/
void prwb();
/**
* print out the image made by ourself and printout user's input simultaneously.
*
* @param read all block and printout.
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.
*/
int checkwin();
/**
* check array which from user's input weather user wining or not and also make a variable for single mode to cupinp().
*
* @param count the "char" which input by playerN if there are 4 "char"s continuously exist playerN will win;
* @return X
* @author made by 406410232_林思妤&連子萱 write by 405630194_林思妤.
*/
void add(int m);
/**
* decide the result ,"who is player1 or player2 or even winner"
*
* @param if (m/10)=1->single mode so,it need to user input 2 names;(m/10)=2->moultiple mode so,it need to user input 1 names and another is computer. 
* (m%10)=1->player 1 win;else player 2 win. 
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.
*/
void add_name(fiPtr *phead, fiPtr *ptail, Name *name, int ro, char *w);
/**
* input the result to remember
*
* @param make user name keep in data and remember the winner.
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.
*/
void prlist(Final *head);
/**
* printout the result .
*
* @param print the result .
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.
*/
void cupinp();
/**
* decide the input for computer which is in single mode.
*
* @param when boolean ,it will input randomly ,or it will defend user to win.
* @return X
* @author made by 406410232_連子萱 write by 405630194_林思妤.
*/
int even();
/**
* when no one wins or no one lose,it will print out the result.
*
* @param when all block is full, print out the result.
* @return X
* @author made by 406410232_連子萱&林思妤 write by 405630194_林思妤.
*/
void save(Final *head);
/**
* when user want to save data, it will save data.
*
* @param save data to round list.txt.
* @return X
* @author made by 406410232_林思妤 write by 405630194_林思妤.

*/
