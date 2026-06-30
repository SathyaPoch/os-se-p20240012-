# commands.md — exact commands I ran, per part

> Paste the **real** commands you ran, in order, in the fenced blocks below. Graded for
> command competency and is your defence if any output is questioned. One block per part.
> Delete the hint comments and replace with your actual commands.

## Part A — Threads, Mapping & Signals

```bash
# compile the threaded program (mind the threading flag), run it
# capture the 1:1 user→kernel (LWP) mapping into thread_map.txt while it runs
# compile/run signal_demo and demonstrate catching the interactive interrupt

# Compile the threaded program with the required pthread flag and run it in the background
gcc -pthread partA_threads/thread_demo.c -o partA_threads/thread_demo
./partA_threads/thread_demo &

# Capture the 1:1 user→kernel (LWP) mapping into thread_map.txt while it runs
ps -efL | grep thread_demo | grep -v grep > partA_threads/thread_map.txt

# Compile and run signal_demo to test the interactive interrupt (Ctrl+C)
gcc partA_threads/signal_demo.c -o partA_threads/signal_demo
./partA_threads/signal_demo
```

## Part B — Permissions, Special Bits & ACLs

```bash
# build the tree (shared dir + private file); set octal + symbolic modes
# demonstrate setgid + sticky on a dir you own; build/set the setuid binary
# add and read back an ACL entry; save reports

mkdir -p partB_security/shared_dir
touch partB_security/private_file

# Set octal mode 600 (owner rw only) on the private file
chmod 600 partB_security/private_file

# Set symbolic mode for traversable but not listable (u=rwx,g=x,o=x) on the shared directory
chmod u=rwx,g=x,o=x partB_security/shared_dir

# Demonstrate setgid (group inheritance) and sticky bit (restricted deletion) on the shared directory
chmod g+s,o+t partB_security/shared_dir

# Build the setuid binary, set the setuid execution bit, and verify permissions
gcc partB_security/setuid_demo.c -o partB_security/setuid_demo
chmod u+s partB_security/setuid_demo

# Save validation parameters and statuses to the report
ls -l partB_security/private_file > partB_security/perm_report.txt
ls -ld partB_security/shared_dir >> partB_security/perm_report.txt
stat partB_security/private_file >> partB_security/perm_report.txt
ls -l partB_security/setuid_demo >> partB_security/perm_report.txt
```

## Part C — Bash Scripting, PATH & Safe Scanning

```bash
# make greeter runnable by name via PATH; record PATH + resolved location
# run collector over your dirs; show it skips unreadable/missing files safely

# Make greeter runnable by name by appending the bin folder to the environment PATH variable
export PATH="$HOME/bin:$PATH"
echo 'export PATH="$HOME/bin:$PATH"' >> ~/.bashrc

# Record PATH variable and the resolved location of the greeter command
echo "PATH Variable Content: $PATH" > partC_scripting/path_report.txt
which greeter >> partC_scripting/path_report.txt

# Run the collector script across the target directories globally via PATH
collector

Part D — Race Condition & flock
Bash

# Initialize the stock file to 150 and clear the logs
echo "150" > partD_secure/stock.txt
> partD_secure/sales.log

# Run the unpatched swarm driver several times and record the flawed remaining stock totals
swarm
echo "Run 1 Unpatched Stock Total remaining: $(cat partD_secure/stock.txt)" > partD_secure/observations.txt

echo "150" > partD_secure/stock.txt
swarm
echo "Run 2 Unpatched Stock Total remaining: $(cat partD_secure/stock.txt)" >> partD_secure/observations.txt

# Reset the stock asset file to 150 after adding the exclusive advisory flock patch
echo "150" > partD_secure/stock.txt
> partD_secure/sales.log

# Re-run the concurrent swarm driver to confirm the deterministic safe fix
swarm
echo "Patched Target Stock Remaining (Should be 100): $(cat partD_secure/stock.txt)" >> partD_secure/observations.txt

Part E — Backups & cron
Bash

# Ensure log target workspace directory structures exist
mkdir -p partE_automation/logs

# E1: Run backup_project 4 consecutive times to force retention pruning (keep newest 3)
backup_project; sleep 1; backup_project; sleep 1; backup_project; sleep 1; backup_project
ls -la partE_automation/backups/

# E2 & E3: Install configurations into your per-user crontab (Recurring, One-shot, and Exam Backups)
crontab -l > my_cron_jobs 2>/dev/null || true
echo "* * * * * $HOME/bin/timed_job $HOME/os-se-p20240012/final-exam/partE_automation/logs/cron_recurring.log" >> my_cron_jobs
echo "35 14 * * * $HOME/bin/timed_job $HOME/os-se-p20240012/final-exam/partE_automation/logs/cron_oneshot.log" >> my_cron_jobs
echo "*/2 * * * * $HOME/bin/backup_exam" >> my_cron_jobs
echo "0 16 * * * $HOME/bin/backup_exam" >> my_cron_jobs
crontab my_cron_jobs
rm my_cron_jobs

# Verify backup destination directories and compile structural outputs into the final report
echo "=== Active Crontab Configuration ===" > partE_automation/cron_report.txt
crontab -l >> partE_automation/cron_report.txt
echo -e "\n=== Recurring Log Sample ===" >> partE_automation/cron_report.txt
cat partE_automation/logs/cron_recurring.log >> partE_automation/cron_report.txt 2>/dev/null
echo -e "\n=== Target Archive Directory States ===" >> partE_automation/cron_report.txt
ls -la ~/exam-backups/ >> partE_automation/cron_report.txt 2>/dev/null
```

## Part D — Race Condition & flock

```bash
# init stock; run swarm several times unpatched and record final stock each time
# add the exclusive advisory lock around the read-modify-write; re-run swarm

# Initialize the stock file to 150 and clear the logs
echo "150" > partD_secure/stock.txt
> partD_secure/sales.log

# Run the unpatched swarm driver several times and record the flawed remaining stock totals
swarm
echo "Run 1 Unpatched Stock Total remaining: $(cat partD_secure/stock.txt)" > partD_secure/observations.txt

echo "150" > partD_secure/stock.txt
swarm
echo "Run 2 Unpatched Stock Total remaining: $(cat partD_secure/stock.txt)" >> partD_secure/observations.txt

# Reset the stock asset file to 150 after adding the exclusive advisory flock patch
echo "150" > partD_secure/stock.txt
> partD_secure/sales.log

# Re-run the concurrent swarm driver to confirm the deterministic safe fix
swarm
echo "Patched Target Stock Remaining (Should be 100): $(cat partD_secure/stock.txt)" >> partD_secure/observations.txt
```

## Part E — Backups & cron

```bash
# E1: run backup_project enough times that pruning happens (keep newest RETAIN_N)
# E2: per-user crontab, two entries (absolute paths):
#     recurring (CRON_INTERVAL) -> partE_automation/logs/cron_recurring.log
#     one-shot at TIMED        -> partE_automation/logs/cron_oneshot.log
# E3: backup_exam -> tar the final-exam folder to ~/exam-backups/final-exam-<ts>.tar.gz
#     crontab: run backup_exam on a short interval AND once at exactly 16:00 today
#     then: ls ~/exam-backups
# capture crontab -l + both logs + the ~/exam-backups listing into cron_report.txt

# Ensure log target workspace directory structures exist
mkdir -p partE_automation/logs

# E1: Run backup_project 4 consecutive times to force retention pruning (keep newest 3)
backup_project; sleep 1; backup_project; sleep 1; backup_project; sleep 1; backup_project
ls -la partE_automation/backups/

# E2 & E3: Install configurations into your per-user crontab (Recurring, One-shot, and Exam Backups)
crontab -l > my_cron_jobs 2>/dev/null || true
echo "* * * * * $HOME/bin/timed_job $HOME/os-se-p20240012/final-exam/partE_automation/logs/cron_recurring.log" >> my_cron_jobs
echo "35 14 * * * $HOME/bin/timed_job $HOME/os-se-p20240012/final-exam/partE_automation/logs/cron_oneshot.log" >> my_cron_jobs
echo "*/2 * * * * $HOME/bin/backup_exam" >> my_cron_jobs
echo "0 16 * * * $HOME/bin/backup_exam" >> my_cron_jobs
crontab my_cron_jobs
rm my_cron_jobs

# Verify backup destination directories and compile structural outputs into the final report
echo "=== Active Crontab Configuration ===" > partE_automation/cron_report.txt
crontab -l >> partE_automation/cron_report.txt
echo -e "\n=== Recurring Log Sample ===" >> partE_automation/cron_report.txt
cat partE_automation/logs/cron_recurring.log >> partE_automation/cron_report.txt 2>/dev/null
echo -e "\n=== Target Archive Directory States ===" >> partE_automation/cron_report.txt
ls -la ~/exam-backups/ >> partE_automation/cron_report.txt 2>/dev/null
```
