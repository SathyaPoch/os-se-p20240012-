/* dir_list_sys.c */
#include <fcntl.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <string.h>
#include <stdio.h>    // only for snprintf to format numbers into strings

int main() {
    char buffer[512];
    // YOUR CODE HERE
    // 1. Open current directory with opendir(".")
    DIR *dir = opendir(".");
    
    // 2. Print header line using write()
    snprintf(buffer, sizeof(buffer), "%-30s %10s\n", "Name", "Size");
    write(1, buffer, strlen(buffer));
    
    // 3. Loop through entries with readdir()
    struct dirent *entry;
    while ((entry = readdir(dir)) != NULL) {
        // 4. For each entry, use stat() to get file size
        struct stat filestat;
        stat(entry->d_name, &filestat);
        
        // 5. Format output into buffer with snprintf(), then write() to fd 1
        snprintf(buffer, sizeof(buffer), "%-30s %10ld\n", entry->d_name, filestat.st_size);
        write(1, buffer, strlen(buffer));
    }
    
    // 6. Close directory with closedir()
    closedir(dir);
    return 0;
}