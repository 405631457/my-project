#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <windows.h>
#include "fu.h" 
#define MAX 10


char all[MAX][MAX];
int mode=1;
char w,b;
int userc=4;
FILE *fps;
int r=0;
/* 
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
*/ 
void clean(){
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            all[i][j]={NULL};
        }
    }
}
void prwb(){
    int count=MAX;
    for(int i=MAX-1;i>=0;i--){
        printf("%02d|",count);
        printf("  ");
        for(int j=0;j<MAX;j++){
            printf(" %c  ",all[i][j]);
        }
        count--;
        printf("\n");
        printf("  |\n");
    }
    for(int i=1;i<=MAX;i++){
        printf("-----");
    }
    printf("\n    ");
    for(int i=1;i<=MAX;i++){
        printf(" %02d ",i);
    }
    printf("\n");
}
int checkwin(){
    int wcount=0;
    int bcount=0;
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                if(i+k >= MAX ) {
                    break;
                }
                if(all[i+k][j]==w) {
                    wcount++;
                }else{
                    break;
                    }
                }
                if(wcount>=userc) return 100;
            }
            wcount=0;
            if(all[i][j]==b){
                for(int s=0;s<MAX; s++){
                   if(i+s >= MAX) {
                      break;
                     }
                   if(all[i+s][j]==b) {
                      bcount++;
                     }else break;
                }
                if(bcount>=userc) return 200;
            }
            bcount=0;
        }

    }
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            wcount=0;
            bcount=0;
            if(all[i][j]==w){
               for(int k=0;k<MAX;k++){
                    if(j+k >= MAX ) {
                        break;
                    }
                    if(all[i][j+k]==w) {
                           wcount++;
                         }else break;
                }
                if(wcount>=userc) return 100;
            }
            if(all[i][j]==b){
                for(int s=0;s<MAX; s++){
                    if(j+s >= MAX) {
                        break;
                    }
                    if(all[i][j+s]==b) {
                        bcount++;
                    }else{
                        break;
                    }
                }
                if(bcount>=userc) return 200;
            }
        }

    }
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            wcount=0;
            bcount=0;
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                    if(i+k >= MAX || j+k >=MAX ) {
                        break;
                    }
                    if(all[i+k][j+k]==w) {
                        wcount++;
                    }else break;
                }
                if(wcount>=userc) return 100;
            }
            if(all[i][j]==b){
                for(int s=0;s<MAX; s++){
                    if(i+s >= MAX || j+s >=MAX ) {
                        break;
                    }
                    if(all[i+s][j+s]==b) {
                        bcount++;
                    }else break;
                }
             if(bcount>=userc) return 200;
             }
        }

    }

      for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            wcount=0;
            bcount=0;
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                    if(i+k >= MAX || j-k <0 ) {
                        break;
                    }
                    if(all[i+k][j-k]==w) {
                        wcount++;
                    }else break;
                }
                if(wcount==userc) return 100;
            }
            if(all[i][j]==b){
                for(int s=0;s<MAX; s++){
                    if(i+s >= MAX || j-s <0 ) {
                        break;
                    }
                    if(all[i+s][j-s]==b) {
                        bcount++;
                    }else break;
                }
             if(bcount>=userc) return 200;
             }
        }

    }
    return 0;
}
void cupinp(){
    int wcount=0;
    int bug=0;
    int boolean=0;
    for(int i=0;i<MAX;i++){                                 //垂直擋 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                if(i+k >= MAX ) {
                    break;
                }
                if(all[i+k][j]==w) {
                    wcount++;
                }else{
                    break;
                    }
                if(wcount==3 && all[i+k+1][j]==NULL){
                    all[i+k+1][j]=b;
                    boolean=1;
                    bug++;
                }
                }
            }
            wcount=0;
        }

    }
    for(int i=0;i<MAX;i++){                                   //水平擋 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
               for(int k=0;k<MAX;k++){
                    if(j+k >= MAX ) {
                        break;
                    }
                    if(all[i][j+k]==w) {
                           wcount++;
					}  else break;
                    if(i-1>=0){     
                      if(wcount==3 && all[i][j+k+1]==NULL && all[i-1][j+k+1]!=NULL && bug==0){	
                         all[i][j+k+1]=b;
                         boolean=1;
                         bug++;
                    }else if(j+k-3>=0){
					if(wcount==3 && all[i][j+k-3]==NULL && all[i-1][j+k-3]!=NULL && all[i][j+k+1]==b && bug==0){	
                         all[i][j+k-3]=b;
                         boolean=1;
                         bug++;
                    } 
					}
                  }else {
                  	if(wcount==3 && all[i][j+k+1]==NULL && bug==0){	
                         all[i][j+k+1]=b;
                         boolean=1;
                         bug++;
                    }else if(j+k-3>=0){
					   if(wcount==3 && all[i][j+k-3]==NULL && all[i][j+k+1]==b && bug==0){	
                         all[i][j+k-3]=b;
                         boolean=1;
                         bug++;
                    }
                    }
				  }
                }
            }
            wcount=0;
            }
        }
        for(int i=0;i<MAX;i++){                         //水平擋中間有空 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(j+2 < MAX && j+3 < MAX) {
                    if(all[i][j+2]==w && all[i][j+3]==w) {
                           wcount=3;
                     }if(i-1>=0){     
                      if(wcount==3 && all[i][j+1]==NULL && all[i-1][j+1]!=NULL && bug==0){	
                         all[i][j+1]=b;
                         boolean=1;
                         bug++;
                    }
                  }else {
                  	if(wcount==3 && all[i][j+1]==NULL && bug==0){	
                         all[i][j+1]=b;
                         boolean=1;
                         bug++;
                    }
				  }
            }
            wcount=0;
           }
            }
        }
        for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(j+1 < MAX && j+3 < MAX) {
                    if(all[i][j+1]==w && all[i][j+3]==w ) {
                           wcount=3;
                     }if(i-1>=0){     
                      if(wcount==3 && all[i][j+2]==NULL && all[i-1][j+2]!=NULL && bug==0){	
                         all[i][j+2]=b;
                         boolean=1;
                         bug++;
                    }
                  }else {
                  	if(wcount==3 && all[i][j+2]==NULL && bug==0){	
                         all[i][j+2]=b;
                         boolean=1;
                         bug++;
                    }
				  }
             }
            wcount=0;
            }
          }
        }
    for(int i=0;i<MAX;i++){                              //右斜擋 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                    if(i+k >= MAX || j+k >=MAX ) {
                        break;
                    }
                    if(all[i+k][j+k]==w) {
                        wcount++;
                    }else break;
                    if(wcount==3 && all[i+k+1][j+k+1]==NULL && all[i+k][j+k+1]!=NULL && bug==0){
                      all[i+k+1][j+k+1]=b;
                      boolean=1;
                      bug++;
                    }
                }
            }
            wcount=0;
            }
        }
     for(int i=0;i<MAX;i++){                         //右斜擋中間有空 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(i+2 < MAX && i+3 < MAX && j+2 < MAX && j+3 < MAX) {
                    if(all[i+2][j+2]==w && all[i+3][j+3]==w) {
                           wcount=3;
                     }if(wcount==3 && all[i+1][j+1]==NULL && all[i][j+1]!=NULL && bug==0){	
                         all[i+1][j+1]=b;
                         boolean=1;
                         bug++;
                    }
				  }
                }
               wcount=0;
           }
     }
    for(int i=0;i<MAX;i++){                         
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(i+1 < MAX && i+3 < MAX && j+1 < MAX && j+3 < MAX) {
                    if(all[i+1][j+1]==w && all[i+3][j+3]==w) {
                           wcount=3;
                     }if(wcount==3 && all[i+2][j+2]==NULL && all[i+1][j+2]!=NULL && bug==0){	
                         all[i+2][j+2]=b;
                         boolean=1;
                         bug++;
                    }
				  }
                }
               wcount=0;
           }
     }
    for(int i=0;i<MAX;i++){                             //左斜擋  
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                for(int k=0;k<MAX;k++){
                    if(j-k<0|| i+k >=MAX ) {
                        break;
                    }if(all[i+k][j-k]==w) {
                        wcount++;
                    }else break;
                    if(wcount==3 && all[i+k+1][j-k-1]==NULL && all[i+k][j-k-1]!=NULL && bug==0){
                      all[i+k+1][j-k-1]=b;
                      boolean=1;
                      bug++;
                    }
                }
            }
            wcount=0;
            }
        }
             for(int i=0;i<MAX;i++){                         //左斜擋中間有空 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(i+2 < MAX && i+3 < MAX && j-2 >= 0 && j-3 >= 0) {
                    if(all[i+2][j-2]==w && all[i+3][j-3]==w) {
                           wcount=3;
                     }if(wcount==3 && all[i+1][j-1]==NULL && all[i][j-1]!=NULL && bug==0){	
                         all[i+1][j-1]=b;
                         boolean=1;
                         bug++;
                    }
				  }
                }
               wcount=0;
           }
     }
    for(int i=0;i<MAX;i++){                         
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(i+1 < MAX && i+3 < MAX && j-1 >= 0 && j-3 >= 0) {
                    if(all[i+1][j-1]==w && all[i+3][j-3]==w) {
                           wcount=3;
                     }if(wcount==3 && all[i+2][j-2]==NULL && all[i+1][j-2]!=NULL && bug==0){	
                         all[i+2][j-2]=b;
                         boolean=1;
                         bug++;
                    }
				  }
                }
               wcount=0;
           }
     }
    for(int i=0;i<MAX;i++){                                   //水平擋2個 旁邊沒東西 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==w){
                    if(j+1 < MAX && j+3 < MAX) {
                    if(all[i][j+1]==w && all[i][j+2]==NULL && all[i][j-1]==NULL) {
                         if(i-1>0) {
						  if(all[i-1][j+2]!=NULL && bug==0){
                         	all[i][j+2]=b;
                         	 boolean=1;
                             bug++;
                         }
						 }else if(bug==0){
						 	all[i][j+2]=b;
						 	 boolean=1;
                             bug++;
						 }
					}
              }
          }
        }
    }
    for(int i=0;i<MAX;i++){                                  //放垂直線 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(i+2 < MAX && i+1 < MAX){ 
                   if(all[i+1][j]==b && all[i+2][j]==NULL && bug==0) {
                   	  all[i+2][j]=b;
                  	  boolean=1;
                  	  bug++;
                     }
            }
          } 
        }
    }
    for(int i=0;i<MAX;i++){                               //放水平    
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(i+1 < MAX && i+2 < MAX && i+3 < MAX) {
                   if(all[i+1][j]==b && all[i+2][j]==b && all[i+3][j]==NULL && bug==0) {
                   	  all[i+3][j]=b;
                  	  boolean=1;
                  	  bug++;
                     }
             }
          } 
        }
    }
    for(int i=0;i<MAX;i++){                                 //放水平  
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
               if(j+1 < MAX && j+2 < MAX ) {
                if(i-1>0){
                   if(all[i][j+1]==b && all[i][j+2]==NULL && all[i-1][j+2]!=NULL && bug==0) {
                   	  all[i][j+2]=b;
                  	  boolean=1;
                  	  bug++;
                     }
                 }else {
                 	if(all[i][j+1]==b && all[i][j+2]==NULL && bug==0) {
                   	  all[i][j+2]=b;
                  	  boolean=1;
                  	  bug++;
                     }
				 }
            }
          } 
        }
    }
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(j+1 < MAX && i+2 < MAX && i+3 < MAX) {
                     if(i-1>0){
                     if(all[i][j+1]==b && all[i][j+2]==b && all[i][j+3]==NULL && all[i-1][j+3]!=NULL && bug==0) {
                   	  all[i][j+3]=b;
                  	  boolean=1;
                  	  bug++;
                      }
                    }else {
                 	if(all[i][j+1]==b && all[i][j+2]==b && all[i][j+3]==NULL && bug==0) {
                   	  all[i][j+3]=b;
                  	  boolean=1;
                  	  bug++;
                     }
				 }
             }
            } 
        }
    }
    for(int i=0;i<MAX;i++){                                             //放右斜 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(i+1 < MAX && j+1 < MAX && i+2 < MAX && j+2 < MAX) { 
                   if(all[i+1][j+1]==b && all[i+2][j+2]==NULL && all[i+1][j+2]!=NULL && bug==0) {
                   	  all[i+2][j+2]=b;
                  	  boolean=1;
                  	  bug++;
                     }
            }
           } 
        }
    }
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(i+1 < MAX && j+1 < MAX && i+2 < MAX && j+2 < MAX && i+3 < MAX && j+3 < MAX ) {
                   if(all[i+1][j+1]==b && all[i+2][j+2]==b && all[i+3][j+3]==NULL && all[i+2][j+3]!=NULL && bug==0) {
                   	  all[i+3][j+3]=b;
                  	  boolean=1;
                  	  bug++;
                     }
            }
          } 
        }
    }
        for(int i=0;i<MAX;i++){                                    //放左斜 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(j-1 >= 0 && i+1 < MAX && j-2 >= 0 && i+2 < MAX) {
                   if(all[i+1][j-1]==b && all[i+2][j-2]==NULL && all[i+1][j-2]!=NULL && bug==0) {
                   	  all[i+2][j-2]=b;
                  	  boolean=1;
                  	  bug++;
                     }
                }
            } 
        }
    }
    for(int i=0;i<MAX;i++){
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if(j-1 >= 0 && i+1 < MAX && j-2 >= 0 && i+2 < MAX && j-3 >= 0 && i+3 < MAX ) {
                   if(all[i+1][j-1]==b && all[i+2][j-2]==b && all[i+3][j-3]==NULL && all[i+2][j-3]!=NULL && bug==0) {
                   	  all[i+3][j-3]=b;
                  	  boolean=1;
                  	  bug++;
                     }
               }
            } 
        }
    }
    for(int i=0;i<MAX;i++){                               //放垂直線1 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if( i+1 >= MAX ) {
                      break;
                     }else if(all[i+1][j]==NULL && bug==0){
                   	  all[i+1][j]=b;
                  	  boolean=1;
                  	  bug++;
                     }
            }
        }
    }
    for(int i=0;i<MAX;i++){                          //放水平線1 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if( j+1 < MAX ) {
				   if(i-1>0){
					  if(all[i][j+1]==NULL && all[i-1][j+1]!=NULL && bug==0){
                   	  all[i][j+1]=b;
                  	  boolean=1;
                  	  bug++;
                     }
					 }else {
                      if(all[i][j+1]==NULL && bug==0){
                   	  all[i][j+1]=b;
                  	  boolean=1;
                  	  bug++;
                     }
					 }
                }
            } 
        }
    }
    for(int i=0;i<MAX;i++){                          //放右斜1 
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if( i+1 < MAX && j+1 < MAX) {
				   if(all[i+1][j+1]==NULL && bug==0){
                   	  all[i+1][j+1]=b;
                  	  boolean=1;
                  	  bug++;
                     }
                }
            } 
        }
    }
    for(int i=0;i<MAX;i++){                         //放左斜1  
        for(int j=0;j<MAX;j++){
            if(all[i][j]==b){
                   if( i+1 < MAX && j-1 >= 0) {
                    if(all[i+1][j-1]==NULL && bug==0){
                   	  all[i+1][j-1]=b;
                  	  boolean=1;
                  	  bug++;
                     }
                  }   
            }
        }
    }
    srand(time(NULL));
    int n;
    if(boolean==0){
    	n=rand()%10;
            for(int stack=0;stack<MAX;stack++){
                if(all[stack][n]==w||all[stack][n]==b){
                }else{
                    all[stack][n]=b;
                    break;
                }
            }
        }

}
int even(){
	int co=0;
	for(int i=0;i<MAX;i++){
		for(int j=0;j<MAX;j++){
			if(all[i][j]!=NULL){
				co++;
			}
		}
	}
	if(co==100){
		printf("everyone is loser!!!\n");
		return 1;
	}

}
void add_name(fiPtr *phead, fiPtr *ptail, Name *name, int ro, char *w){ 
	Final *tmp_final = (struct Final*)malloc(sizeof(struct Final));
	tmp_final->userName = name;
	tmp_final->round=ro;
	tmp_final->winner=w;
	tmp_final->next = NULL;
	if(ro==0)
	{
	  *phead = tmp_final;
	  *ptail = tmp_final;
	}else{
	  (*ptail)->next= tmp_final;
	  *ptail =tmp_final;
    }
}
void add(int m) {
	char *pl1 = (char *)malloc(20);
	char *pl2 = (char *)malloc(20);
	char *win = (char *)malloc(20);
	printf("input player1's name: ");
	scanf("%s",pl1);
	if((m/10)==2){
		printf("input player2's name: ");
		scanf("%s",pl2);
	}else if((m/10)==1){
		pl2="computer";
	}
	if(m%10==1){
		win=pl1;
	}else if(m%10==2){
		win=pl2;
	}
	Name *tmp_name = (struct Name *)malloc(sizeof(struct Name));
	tmp_name->player1=pl1;
	tmp_name->player2=pl2;
	add_name(&head, &tail, tmp_name, r, win);
}

void prlist(Final *head){
	Final *temp = head;
	for(int i=0; i<r; i++)
	{
		printf("Round : %02d  ,Player1: %-2s  ,Player2: %-2s  ,Winner: %-2s.\n", i + 1, temp->userName->player1,temp->userName->player2 ,temp->winner);
	    if((temp->next)!= NULL)
		{
			temp= temp->next;
		}else
		{
			break;
		}
	}
}
void save(Final *head){
	char filename[100]="round list.txt";
	fps=fopen(filename,"w");
	if(fps==NULL){
        printf("Error\n");
        exit(1);
    }
	printf("data is saving......\n");
	Final *temp = head;
	for(int i=0; i<r; i++)
	{	
	
		fprintf(fps,"Round : %02d  ,Player1: %-2s  ,Player2: %-2s  ,Winner: %-2s.\n", i + 1, temp->userName->player1,temp->userName->player2 ,temp->winner);
	    if(temp->next != NULL)
		{
			temp = temp->next;
		}else
		{
			break;
		}
	}
	
	printf("data is completed-saved!!!\n");
	fclose(fps);
}

int main() {
           int ft=0;
           printf("遊戲規則:\n");
           printf("10*10的棋盤，兩名玩家分別持有不同的符號\n");
           printf("玩家選擇其中一直行下、若那一直行底下沒東西，會在最底層，若有，則會堆積\n");
           printf("在棋盤裡，不論水平、垂直或斜線，誰的符號先完成四個連線者，則該玩家獲勝\n");
            while(mode>0){
                int n;
                int check;
                int evenc;
                printf("menu:\n");
                printf("-1.exit\n");
                printf("1.single\n");
                printf("2.multiple\n");
                if(ft>0){
                	printf("3.round list(or save list)\n");
				}
                printf("Choose a mode: ");
                scanf("%d",&mode);
                if(mode!=3&&mode!=-1){
                	printf("Choose a character for first:\n");
                	scanf("%s",&w);
                	printf("Choose a character for secondary:\n");
               		scanf("%s",&b);
				}
                ft++;
                    switch(mode){
                        case 2:
                             while(1){
                                printf("input the veri you want to put (player1): ");
                                scanf("%d",&n);
                                if(n>10 || n<0){
                                    printf("error!!!\nPlease input again\n");
                                     printf("input the veri you want to put (player1): ");
                                    scanf("%d",&n);
								}
                                n=n-1;
                                for(int stack=0;stack<MAX;stack++){
                                    if(all[stack][n]==w||all[stack][n]==b){
                                }else{
                                    all[stack][n]=w;
                                    check=checkwin();
                                    prwb();
                                    break;
                                    }
                                }
                                  if(check!=100){
                                      printf("input the veri you want to put (player2): ");
                                    scanf("%d",&n);
                                    if(n>10 || n<0){
                                    printf("error!!!\nPlease input again\n");
                                     printf("input the veri you want to put (player2): ");
                                    scanf("%d",&n);
							       }
                                    n=n-1;
                                      for(int stack=0;stack<MAX;stack++){
                                        if(all[stack][n]==w||all[stack][n]==b){
                                         }else{
                                            all[stack][n]=b;
                                            prwb();
                                            break;
                                            }
                                        }
                                  }
                            check=checkwin();
							
							evenc=even();
							if(evenc==1){
								break;
							}
                            if(check==100){
                                 printf("player1 win\n");
                                 printf("player2 lose\n");
                                 add(21);
                                 r++;
                                break;
                                }else if(check==200){
                                      printf("player1 lose\n");
                                      printf("player2 win\n");
                                      add(22);
                                      r++;
                                      break;
                                   }
                        }
                        break;
                        case 1:
                                while(1){
                                    printf("input the veri you want to put (player): ");
                                    scanf("%d",&n);
                                    if(n>10 || n<0){
                                    printf("error!!!\nPlease input again\n");
                                     printf("input the veri you want to put (player): ");
                                    scanf("%d",&n);
									}
                                    n=n-1;
                                    for(int stack=0;stack<MAX;stack++){
                                        if(all[stack][n]==w||all[stack][n]==b){
                                        }else{
                                            all[stack][n]=w;
                                            prwb();
                                            break;
                                        }
                                    }
                                    check=checkwin();

                                      if(check!=100){
                                          printf("computer is inputting......\n");
                                          Sleep( 1000 );
                                          cupinp();
                                          prwb();
                                    }
                                    check=checkwin();
									evenc=even();
									if(evenc==1){
										break;
									}
                                    if(check==100){
                                      printf("player win\n");
                                      printf("computer lose\n");
                                      add(11);
                                      r++;
                                      break;
                                        }else if(check==200){
                                            printf("user lose\n");
                                            printf("computer win\n");
                                            add(12);
                                            r++;
                                              break;
                                           }
                                }
                                break;
                        case 3:
                        	char s;
                        	prlist(head);
                        	printf("Do you want to save?(y/n)\n");
                        	scanf("%s",&s);
                        	if(s=='y'||s=='Y'){
                        		save(head);
							}else{
								break;
							}
                        	break;
                            }
                clean();

                }

}
