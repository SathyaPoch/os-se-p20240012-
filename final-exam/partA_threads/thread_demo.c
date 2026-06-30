#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

void* worker_func(void* arg) {
    long id = (long)arg;
    printf("Worker Thread %ld: Alive. LWP ID: %d\n", id, (int)gettid());
    
    // Sleep briefly so you have time to capture the kernel mapping in another terminal
    sleep(10); 
    
    long* result = malloc(sizeof(long));
    *result = id * 100;
    pthread_exit((void*)result);
}

int main() {
    pthread_t threads[5];
    printf("Main Thread: Spawning 5 workers. Main PID: %d\n", getpid());
    
    for (long i = 0; i < 5; i++) {
        pthread_create(&threads[i], NULL, worker_func, (void*)i);
    }
    
    long final_summary = 0;
    for (int i = 0; i < 5; i++) {
        long* res;
        pthread_join(threads[i], (void**)&res);
        final_summary += *res;
        free(res);
    }
    
    printf("Final Summary Result: %ld\n", final_summary);
    return 0;
}
