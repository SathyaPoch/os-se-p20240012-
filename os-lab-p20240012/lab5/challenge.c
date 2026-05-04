#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>

volatile int keep_running = 1;

void sig_handler(int signo) {
    if (signo == SIGINT) {
        printf("\n[Signal Caught] Received SIGINT (Ctrl+C). Setting keep_running = 0...\n");
        keep_running = 0;
    }
}

void* worker_thread(void* arg) {
    pthread_t tid = pthread_self();
    
    while (keep_running == 1) {
        printf("Worker Thread [ID: %lu] is running...\n", (unsigned long)tid);
        sleep(1);
    }
    
    printf("Worker Thread [ID: %lu] detected shutdown flag. Exiting...\n", (unsigned long)tid);
    pthread_exit(NULL);
}

int main() {
    pthread_t thread1, thread2;

    if (signal(SIGINT, sig_handler) == SIG_ERR) {
        printf("Error: Cannot catch SIGINT\n");
        return 1;
    }

    printf("Main thread started. Spawning worker threads...\n");
    printf("Press Ctrl+C to test graceful shutdown.\n\n");

    if (pthread_create(&thread1, NULL, worker_thread, NULL) != 0) {
        printf("Failed to create thread 1\n");
        return 1;
    }
    if (pthread_create(&thread2, NULL, worker_thread, NULL) != 0) {
        printf("Failed to create thread 2\n");
        return 1;
    }

    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);

    printf("All threads cleanly exited. Goodbye.\n");

    return 0;
}