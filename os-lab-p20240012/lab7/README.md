# OS Lab 7 Submission — Bash Scripting, Permissions & Server Automation

- **Student Name:** Poch Sathya
- **Student ID:** p20240012

---

## Task Output Files

Make sure all of the following files are present in your `lab7/` folder:

- [ ] `task1_warmup.txt`
- [ ] `task2_path.txt`
- [ ] `task3_doorstep.txt`
- [ ] `task4_inbox.txt`
- [ ] `task5_broadcaster.txt`
- [ ] `task6_guestbook.txt`
- [ ] `harvest_report.txt`
- [ ] `task8_mailman.txt`
- [ ] `sign_book.c`
- [ ] `scripts/warmup`
- [ ] `scripts/broadcaster`
- [ ] `scripts/harvester`
- [ ] `scripts/mailman`
- [ ] `scripts/sign_book_binary`

---

## Screenshots

Insert your screenshots below.

### Screenshot 1 — Task 1: Warm-Up Script
Show `cat task1_warmup.txt` with the executable `warmup` script and successful output.

![warmup](images/task1_warmup.png)

---

### Screenshot 2 — Task 2: PATH Setup
Show `cat task2_path.txt` with your `PATH`, `which warmup`, and running `warmup` by name.

![path](images/task2_path.png)

---

### Screenshot 3 — Task 3: Doorstep Message
Show `cat task3_doorstep.txt` with username, users online, uptime, and random quote.

![doorstep](images/task3_doorstep.png)

---

### Screenshot 4 — Task 4: Secure Mailbox
Show `cat task4_inbox.txt` with `public_inbox` permissions and a test file from a classmate.

![inbox](images/task4_inbox.png)

---

### Screenshot 5 — Task 5: Broadcaster
Show `cat task5_broadcaster.txt` with the broadcaster script evidence and `secret.txt`.

![broadcaster](images/task5_broadcaster.png)

---

### Screenshot 6 — Task 6: VIP Guestbook
Show `cat task6_guestbook.txt` with guestbook permissions, SUID binary permissions, and guestbook contents.

![guestbook](images/task6_guestbook.png)

---

### Screenshot 7 — Task 7: Data Harvester
Show `cat harvest_report.txt` containing secrets collected from classmates.

![harvester](images/task7_harvester.png)

---

### Screenshot 8 — Task 8: Mailman Bot
Show `cat task8_mailman.txt` with mailman output and messages received in your inbox.

![mailman](images/task8_mailman.png)

---

## Answers to Lab Questions

1. **Why did `warmup` fail before you added execute permission?**
   > Linux creates files as plain text by default for security. You have to explicitly add execute (+x) permissions so the operating system knows it is allowed to run as a program.

2. **What does adding `~/bin` to `PATH` allow you to do?**
   > It lets you run your personal scripts from anywhere in the terminal just by typing the script's name, without needing to type the full path (like ./warmup).

3. **Why does `chmod 733 public_inbox` allow classmates to drop files but not list the inbox?**
   > The 3 gives them write (w) and execute (x) permissions. Execute lets them enter the directory, and write lets them add files. However, because they lack read (r) permission, they can't use ls to see what is already inside.

4. **Why does Linux ignore SUID on shell scripts, and why did we use a compiled C program instead?**
   > Giving SUID to shell scripts is a massive security risk because attackers can easily manipulate the shell environment variables to run malicious code. We used a compiled C program because it is self-contained and much harder to hijack.

5. **What is the difference between `>` and `>>` in Bash redirection?**
   >completely overwrites the target file with new data. >> appends the new data to the bottom of the existing file without deleting what is already there.

6. **How did your `harvester` avoid reading files that were missing or not readable?**
   >By using a conditional test (like if [ -r filename ]) to check if the file existed and was readable before trying to open it, or by redirecting the errors away (using 2>/dev/null).

7. **What permission problems did you or your classmates need to fix during the lab?**
   > The most common issue was just forgetting to run chmod +x on scripts before testing them, and fixing directory permissions so teammates could actually access and share files properly without getting "Permission denied" errors.

---

## Reflection

> _What did this lab teach you about combining scripting, permissions, and automation on a shared Linux server?_ I learned to set permission so that other people could edit files in my directory ,
