package activity6.task1_deadlock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

class Account {
    String name;
    int balance;
    Semaphore lock = new Semaphore(1);

    Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
}

class Transfer {

    // Flags used by the watchdog to determine whether transfers completed
    static final AtomicBoolean t1Completed = new AtomicBoolean(false);
    static final AtomicBoolean t2Completed = new AtomicBoolean(false);

    static void transfer(Account from, Account to,
                         int amount,
                         AtomicBoolean completionFlag) {

        boolean fromLocked = false;
        boolean toLocked = false;

        try {
            System.out.println(Thread.currentThread().getName()
                    + " trying to lock FROM " + from.name);

            from.lock.acquire();
            fromLocked = true;

            System.out.println(Thread.currentThread().getName()
                    + " locked FROM " + from.name);

            // Artificial delay to increase the chance of deadlock
            Thread.sleep(200);

            System.out.println(Thread.currentThread().getName()
                    + " trying to lock TO " + to.name);

            to.lock.acquire();
            toLocked = true;

            System.out.println(Thread.currentThread().getName()
                    + " locked TO " + to.name);

            // Perform transfer
            from.balance -= amount;
            to.balance += amount;

            System.out.println(Thread.currentThread().getName()
                    + " transfer completed");

            completionFlag.set(true);

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()
                    + " was interrupted.");

            Thread.currentThread().interrupt();

        } finally {
            // Release locks if they were acquired
            if (toLocked) {
                to.lock.release();
            }

            if (fromLocked) {
                from.lock.release();
            }
        }
    }
}

public class DeadlockSimulation {

    public static void main(String[] args) {

        Account account1 = new Account("Account-1", 1000);
        Account account2 = new Account("Account-2", 1000);

        // Print starting balances
        System.out.println("=== Starting Balances ===");
        System.out.println(account1.name + " balance: $" + account1.balance);
        System.out.println(account2.name + " balance: $" + account2.balance);
        System.out.println("=========================\n");

        Thread t1 = new Thread(
                () -> Transfer.transfer(
                        account1,
                        account2,
                        100,
                        Transfer.t1Completed),
                "Thread-1"
        );

        Thread t2 = new Thread(
                () -> Transfer.transfer(
                        account2,
                        account1,
                        200,
                        Transfer.t2Completed),
                "Thread-2"
        );

        // Start transaction threads
        t1.start();
        t2.start();

        // Watchdog thread to detect deadlock
        Thread watchdog = new Thread(() -> {
            try {
                // Wait long enough to confirm threads are stuck
                Thread.sleep(3000);

                if (!Transfer.t1Completed.get()
                        && !Transfer.t2Completed.get()) {

                    System.out.println(
                            "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(
                            "Deadlock detected: transactions are stuck");
                    System.out.println(
                            "Thread-1 is waiting for " + account2.name);
                    System.out.println(
                            "Thread-2 is waiting for " + account1.name);
                    System.out.println(
                            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                    System.out.println(
                            "\n=== Final Balances (Deadlock State) ===");
                    System.out.println(
                            account1.name + " balance: $" + account1.balance);
                    System.out.println(
                            account2.name + " balance: $" + account2.balance);

                    // Exit so the program doesn't hang forever
                    System.exit(0);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        watchdog.setDaemon(true);
        watchdog.start();
    }
}