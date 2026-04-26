#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    pid_t pid = fork();
    if (pid > 0) {
        printf("Parent (PID %d) sleeping for 10s...\n", getpid());
        sleep(10);
        wait(NULL); // Reaps the zombie
        printf("Parent: Child reaped. Check ps now.\n");
        sleep(5); 
    } else {
        printf("Child (PID %d) exiting.\n", getpid());
        exit(0);
    }
    return 0;
}