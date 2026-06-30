#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

void handle_signal(int sig) {
    if (sig == SIGINT) {
        printf("\n[SIGNAL CATCH] Intercepted SIGINT (Ctrl+C). Cleaning up memory caches...\n");
    } else if (sig == SIGTERM) {
        printf("\n[SIGNAL CATCH] Intercepted SIGTERM (Polite Quit). Cleaning up processes...\n");
    }
    printf("System Status: Clean. Exiting gracefully now.\n");
    exit(0);
}

int main() {
    signal(SIGINT, handle_signal);
    signal(SIGTERM, handle_signal);
    
    printf("Signal Demo running. Target PID: %d. Loop active... (Press Ctrl+C to test)\n", getpid());
    while(1) {
        sleep(1);
    }
    return 0;
}
