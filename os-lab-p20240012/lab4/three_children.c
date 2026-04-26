#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    for (int i = 0; i < 3; i++) {
        if (fork() == 0) {
            printf("Child %d (PID %d) sleeping...\n", i+1, getpid());
            sleep(20);
            exit(0);
        }
    }
    // Parent waits for all 3
    for (int i = 0; i < 3; i++) wait(NULL);
    printf("All children finished.\n");
    return 0;
}