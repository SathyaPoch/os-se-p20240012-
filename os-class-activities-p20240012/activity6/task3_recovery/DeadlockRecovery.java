package activity6.task3_recovery;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

    static void transfer(Account from, Account to, int amount) {

        while (true) {
            boolean fromLocked = false;
            boolean toLocked = false;

            try {
                // Try to lock first account
                from.lock.acquire();
                fromLocked = true;

                System.out.println(Thread.currentThread().getName()
                        + " locked " + from.name);

                // Simulate processing delay
                Thread.sleep(100);

                // Try to lock second account with timeout
                toLocked = to.lock.tryAcquire(500, TimeUnit.MILLISECONDS);

                if (!toLocked) {
                    System.out.println(Thread.currentThread().getName()
                            + " could not lock " + to.name
                            + ", releasing " + from.name
                            + " and retrying...");

                    from.lock.release();
                    fromLocked = false;

                    Thread.sleep(200);
                    continue;
                }

                System.out.println(Thread.currentThread().getName()
                        + " locked " + to.name);

                // Perform transfer
                from.balance -= amount;
                to.balance += amount;

                System.out.println(Thread.currentThread().getName()
                        + " transferred $" + amount
                        + " from " + from.name
                        + " to " + to.name);

                break;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;

            } finally {
                if (toLocked) {
                    to.lock.release();
                }

                if (fromLocked) {
                    from.lock.release();
                }
            }
        }
    }
}

public class DeadlockRecovery {

    public static void main(String[] args) {

        Account accountA = new Account("A", 1000);
        Account accountB = new Account("B", 1000);

        int startingTotal = accountA.balance + accountB.balance;

        System.out.println("Starting total: " + startingTotal);

        Thread worker1 = new Thread(
                () -> Transfer.transfer(accountA, accountB, 100),
                "Worker 1"
        );

        Thread worker2 = new Thread(
                () -> Transfer.transfer(accountB, accountA, 200),
                "Worker 2"
        );

        worker1.start();
        worker2.start();

        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int finalTotal = accountA.balance + accountB.balance;

        System.out.println("\n=== Final Balances ===");
        System.out.println("Final A: " + accountA.balance);
        System.out.println("Final B: " + accountB.balance);
        System.out.println("Final total: " + finalTotal);

        System.out.println("Deadlock recovery completed successfully.");
    }
}