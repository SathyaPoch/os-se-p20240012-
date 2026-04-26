# Class Activity 3 — Socket Communication & Multithreading

- **Student Name:** Poch Sathya
- **Student ID:** p20240012
- **Date:** 02/04/2026

---

## Task 1: TCP Socket Communication (C)

### Compilation & Execution

![Socket exchange](screenshots/task1_socket_exchange.png)

### Answers

1. **Role of `bind()` / Why client doesn't call it:**
   > bind() gives the server a fixed address/port so clients can find it. Clients don't need it because the OS assigns them a random available port automatically.

2. **What `accept()` returns:**
   > It returns a new socket file descriptor used only for that specific client, keeping the original socket free to listen for more connections.

3. **Starting client before server:**
   > The client will fail and show a "Connection Refused" error because no server is listening for the request yet.
4. **What `htons()` does:**
   > It converts the port number to Network Byte Order. This ensures computers with different architectures can understand the port number correctly.
5. **Socket call sequence diagram:**
   > Server: socket → bind → listen → accept → read/write

    Client: socket → connect → write/read

---

## Task 2: POSIX Threads (C)

### Output — Without Mutex (Race Condition)

![Threads output](screenshots/task2_threads_output.png)

### Output — With Mutex (Correct)


### Answers

1. **What is a race condition?**
   > When multiple threads access shared data at the same time, causing unpredictable results depending on which thread finishes first.

2. **What does `pthread_mutex_lock()` do?**
   > It locks a section of code so only one thread can run it at a time, preventing data corruption.
3. **Removing `pthread_join()`:**
   > The main() function might finish and exit before the threads even have a chance to complete their work.
4. **Thread vs Process:**
   > A process is a full program with its own memory; a thread is a "mini-process" inside it that shares memory with other threads.
---

## Task 3: Java Multithreading

### ThreadDemo Output

![Java threading](screenshots/task3_java_output.png)

### RunnableDemo Output

![Java threading](screenshots/task3_runnable_output.png)

### PoolDemo Output

![Java threading](screenshots/task3_pool_output.png)

### Answers

1. **Thread vs Runnable:**
   > Thread is a class you extend (limited to one); Runnable is an interface you implement (more flexible for inheritance).

2. **Pool size limiting concurrency:**
   > It limits how many tasks run at once. If the pool is 2, and you have 5 tasks, 3 must wait in a queue until a thread is free.

3. **thread.join() in Java:**
   > It forces the current thread to wait until the target thread finishes its execution.
4. **ExecutorService advantages:**
   > It manages a pool of threads for you, saving the overhead of constantly creating and destroying threads.

## Task 4: Observing Threads

### Linux — `ps -eLf` Output


_(Paste the relevant ps output here)_

### Linux — htop Thread View

![htop threads](screenshots/task4_htop_threads.png)

### Windows — Task Manager

![Task Manager threads](screenshots/task4_taskmanager_threads.png)

### Answers

1. **LWP column meaning:**
   > Lightweight Process. It is the unique ID the Linux kernel gives to each individual thread.

2. **/proc/PID/task/ count:**
   > Each subdirectory inside task/ represents one thread. Counting them shows the total thread count for that process.

3. **Extra Java threads:**
   > These are background system threads for things like Garbage Collection (GC) and the JIT compiler.

4. **Linux vs Windows thread viewing:**
   > Linux uses ps -eLf or htop. Windows uses Task Manager or Resource Monitor (with the "Threads" column enabled).
---

## Reflection

> _it was interesting to see how sockets make network communication feel like just reading a file .Understanding threads at the OS level shows why mutexes are necessary to stop data from breaking.this helps me write faster, more stable code that handles multiple tasks properly. 