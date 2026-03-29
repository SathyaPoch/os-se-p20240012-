# Class Activity 1 — System Calls in Practice

- **Student Name:** Poch Sathya
- **Student ID:** p20240012
- **Date:** 30/03/2026

---

## Warm-Up: Hello System Call

Screenshot of running `hello_syscall.c` on Linux:

![Hello syscall](screenshots/Example1.jpg)

Screenshot of running `hello_winapi.c` on Windows (CMD/PowerShell/VS Code):

![Hello WinAPI](screenshots/hello_winapi.jpg)

Screenshot of running `copyfilesyscall.c` on Linux:

![Copy file syscall](screenshots/Example3.png)

---

## Task 1: File Creator & Reader

### Part A — File Creator

**Describe your implementation:** [What differences did you notice between the library version and the system call version?]

    The differences between the library version and the library version is :
    -the system version communicate with the operating system kernal directly
    -the library version communication with C libary.

**Version A — Library Functions (`file_creator_lib.c`):**

<!-- Screenshot: gcc -o file_creator_lib file_creator_lib.c && ./file_creator_lib && cat output.txt -->
![Task 1A - Library](screenshots/file_creator_lib.png)

**Version B — POSIX System Calls (`file_creator_sys.c`):**

<!-- Screenshot: gcc -o file_creator_sys file_creator_sys.c && ./file_creator_sys && cat output.txt -->
![Task 1A - Syscall](screenshots/file_creator_sys.png)

**Questions:**

1. **What flags did you pass to `open()`? What does each flag mean?**

   > The meaning of each flag :
    -O_WRONLY (Write Only): tells the OS to open the file strictly for writing
    -O_CREAT (Create): tells the OS that if 'output.txt' does not exist yet, create it.
    -O_TRUNC (Truncate): tells the OS that if 'output.txt' already exists, delete everything inside it and shrink its size to 0 bytes before I start writing.


2. **What is `0644`? What does each digit represent?**

   > Each digit represents:
   -Leading 0: indicates the number is in octal (base-8) notation, which is standard for Unix  permissions.
   -6 (Owner/User):the creator of the file can read and write.
   -4 (Group): others in the same group can read.
   -4 (Others/World): everyone else can only read.

3. **What does `fopen("output.txt", "w")` do internally that you had to do manually?**


   > When we call fopen("output.txt", "w"), it abstracts several low-level tasks and system calls that you would have to manage manually using unbuffered I/O functions or direct system calls (like open, write, close in POSIX systems). 

   What we have to do manually:
   -Error Checking
   -Memory Management for Buffering
   -Manual I/O Operations
   -File State Management
   -Resource Cleanup

### Part B — File Reader & Display

**Describe your implementation:** 
1. Three flags were passed, combined using the bitwise OR operator (|):

    O_WRONLY: Opens the file strictly for writing (reading is blocked).

    O_CREAT: Tells the OS to create the file if it does not already exist.

    O_TRUNC: Tells the OS to truncate (wipe clean) the file to 0 bytes if it already exists, ensuring you start with a completely blank slate.
2. 0644 is an octal (base-8) number that sets the security permissions for the newly created file.

    0 (Prefix): Tells the C compiler that this is an octal number, not a standard decimal.

    6 (Owner): Read (4) + Write (2) = 6. The user who created the file can read and modify it.

    4 (Group): Read (4) = 4. Other users in the owner's user group can only read the file.

    4 (Others/World): Read (4) = 4. Everyone else on the system can only read the file.
3. fopen() acts as a high-level wrapper that automates the tedious setup required by open(). Internally, it does the following:

    Translates Flags: It automatically converts the simple "w" string into the exact same O_WRONLY | O_CREAT | O_TRUNC system flags.

    Handles Permissions: It automatically requests default creation permissions (which the system safely downgrades to 0644), so you don't have to provide them manually.

    Allocates Memory: It uses malloc() to build a complex FILE struct in RAM to track the file descriptor, error states, and end-of-file indicators.

    Sets up Buffering: Crucially, it creates a temporary memory buffer (usually 4KB). Instead of making a slow system call every time you want to write a character, it collects your text in this RAM buffer and sends it to the OS in one massive, highly efficient chunk.

**Version A — Library Functions (`file_reader_lib.c`):**

![Task 1B - Library](screenshots/file_reader_lib.png)

**Version B — POSIX System Calls (`file_reader_sys.c`):**

![Task 1B - Syscall](screenshots/file_reader_sys.png)

**Questions:**

1. **What does `read()` return? How is this different from `fgets()`?**

   > 'read()' return 0 if error it return -1. 
    'read()' is difference from 'fgets()' because :
        fgets() is a standard, portable library function that reads line-oriented, buffered data from a FILE * stream, while read() is a non-standard POSIX system call that reads raw, unbuffered bytes from a file descriptor (int fildes).
     

2. **Why do you need a loop when using `read()`? When does it stop?**

   > Using a loop for 'read()' is required because the number of bytes read may be less than the number of bytes requested.

---

## Task 2: Directory Listing & File Info

**Describe your implementation:** 
1. 'read()' return 0 , and If an error occurs, it returns -1.
2. A loop is necessary when using the 'read()' system call in C because read() is not guaranteed to read the requested number of bytes in a single call. It can return a "short count," indicating that only some of the bytes were read, which is not an error condition. 
    The loop stops when read() returns 0.

### Version A — Library Functions (`dir_list_lib.c`)

![Task 2 - Version A](screenshots/dir_list_lib.png)

### Version B — System Calls (`dir_list_sys.c`)

![Task 2 - Version B](screenshots/dir_list_sys.png)

### Questions

1. **What struct does `readdir()` return? What fields does it contain?**

   >  When you call readdir(), it returns a pointer to a struct dirent.If there are no more files to read in the directory, or if an error occurs, it returns NULL.

2. **What information does `stat()` provide beyond file size?**

   > The stat() function in C provides comprehensive metadata about a file via the struct stat structure, extending far beyond just file size (st_size). Key information includes file type.

3. **Why can't you `write()` a number directly — why do you need `snprintf()` first?**

   > We can't 'write()' a number directly because the 'write()' system call is a low-level function that outputs raw bytes to a file or device, not the human-readable character representation of a number. 'snprintf()' is necessary to first convert the integer value's binary representation into its corresponding string of ASCII characters
---

## Optional Bonus: Windows API (`file_creator_win.c`)

Screenshot of running on Windows:

![Task 1 - Windows](screenshots/task1_win.png)

### Bonus Questions

1. **Why does Windows use `HANDLE` instead of integer file descriptors?**

   > 

2. **What is the Windows equivalent of POSIX `fork()`? Why is it different?**

   > 

3. **Can you use POSIX calls on Windows?**

   > 

---

## Task 3: strace Analysis

**Describe what you observed:** [What surprised you about the strace output? How many more system calls did the library version make?]


### strace Output — Library Version (File Creator)


![strace - Library version File Creator](screenshots/strace_lib_task1.png)

### strace Output — System Call Version (File Creator)


![strace - System call version File Creator](screenshots/strace_sys_task1.png)

### strace Output — Library Version (File Reader or Dir Listing)

![strace - Library version](screenshots/strace_lib_reader.png)

### strace Output — System Call Version (File Reader or Dir Listing)

![strace - System call version](screenshots/strace_sys_reader.png)

### strace -c Summary Comparison


![strace summary - Library](screenshots/strace_summary_lib.png)

![strace summary - Syscall](screenshots/strace_summary_sys.png)

### Questions

1. **How many system calls does the library version make compared to the system call version?**

   > The library version and the system call version have the same number of system calls which is 142 in total.

2. **What extra system calls appear in the library version? What do they do?**

   > Extra system calls in the library version :
   -fstat (or newfstatat): Used by fopen to inspect the file's metadata on the hard drive. It specifically looks for the st_blksize (optimal block size) so it knows exactly how large to make its memory buffer.

    -brk: Used to allocate memory on the heap. When fopen uses malloc() to create the FILE struct and its internal 4KB buffer, malloc() uses the brk system call to request that RAM from the Operating System.

    -mmap: Another memory allocation system call. While brk extends the heap, mmap is used to map larger chunks of memory or load shared dynamic libraries into the program's memory space.


3. **How many `write()` calls does `fprintf()` actually produce?**

   > 'fprintf()' produce 2 'write()' system calls.

4. **In your own words, what is the real difference between a library function and a system call?**

   > For library function it calls the program while a system call communicate directly to the kernal.

---

## Task 4: Exploring OS Structure

### System Information

> 📸 Screenshot of `uname -a`, `/proc/cpuinfo`, `/proc/meminfo`, `/proc/version`, `/proc/uptime`:

![System Info](screenshots/task4-A.1.jpg)

![System Info](screenshots/task4-A.2.jpg)

![System Info](screenshots/task4-A.3.jpg)

![System Info](screenshots/task4-A.4.jpg)


### Process Information

> 📸 Screenshot of `/proc/self/status`, `/proc/self/maps`, `ps aux`:

![Process Info](screenshots/task4-B.1.jpg)
<br></br>

![Process Info](screenshots/task4-B.2.jpg)
<br></br>

![Process Info](screenshots/task4-B.4.jpg)



### Kernel Modules

> 📸 Screenshot of `lsmod` and `modinfo`:

![Kernel Modules](screenshots/task4-C.1.jpg)
<br></br>

![Kernel Modules](screenshots/task4-C.2.jpg)

### OS Layers Diagram

> 📸 Your diagram of the OS layers, labeled with real data from your system:

![OS Layers Diagram](screenshots/OS_diagram.jpg)

### Questions

1. **What is `/proc`? Is it a real filesystem on disk?**

   > No, /proc is not a real filesystem stored on a physical hard drive. It is a virtual filesystem (often called a pseudo-filesystem) that is created dynamically by the Linux kernel and exists entirely in RAM.

2. **Monolithic kernel vs. microkernel — which type does Linux use?**

   > Comparing Monolithic kernel and microkernel :

    -Monolithic kernels (e.g., Linux, Windows) run all operating system services—drivers, file systems, memory management—within a single, high-performance address space.

    -Microkernels (e.g., Minix, QNX) run only essential services in kernel space, pushing others to user space to improve stability, security, and modularity at the cost of performance.
    +Linux uses a monolithic kernel.

3. **What memory regions do you see in `/proc/self/maps`?**

   > Memory regions with no file path attached (the rows ending with just 00:00 0).

4. **Break down the kernel version string from `uname -a`.**

   > The kernal version is 6.8.0-106-generic.

5. **How does `/proc` show that the OS is an intermediary between programs and hardware?**

   > Here is how /proc shows that the operating system (OS) is an intermediary:

    -Hardware Abstraction (/proc/cpuinfo, /proc/meminfo): Programs do not need to know the specific manufacturer or model of the processor or RAM to use them. The OS collects this information and presents it in a standardized, text-based format in /proc/cpuinfo and /proc/meminfo. 

    -Virtual Process View (/proc/[pid]/): When a program runs, the OS creates a directory in /proc corresponding to its Process ID (PID). This directory (e.g., /proc/1234/) provides a tailored, safe view of the resources only that process is allowed to see, hiding the complexity of the entire system's memory management and CPU scheduling.

    -Real-time Kernel Control and Status: The kernel uses files in /proc/sys/ to expose tunable parameters. Programs can read these files to check system status or write to them to change behavior, such as modifying kernel parameters, without needing to interact directly with hardware drivers.

    -Safety and Protection: Because /proc is virtual and resides in memory (not on a disk), applications cannot break the system by editing a "real" file. 

    -Uniformity: Whether the system has an Intel or AMD CPU, a mechanical hard drive or an SSD, the /proc entries for hardware status remain consistent. The OS bridges the gap between diverse hardware and the uniform needs of software.

---

## Reflection

What did you learn from this activity? What was the most surprising difference between library functions and system calls?

> I did learn some basics C programming and still not quite familiar with C programming yet.And also have a better understanding of kernal,system calls and C library.I'm not aware of the fact that we could write a system call to communicate directly with the kernal which caught me off guard.