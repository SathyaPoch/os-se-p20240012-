package activity6.task2_prevention;
import java.util.concurrent.Semaphore;

class Account {
    String name;
    int balance;

    Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
}

class Transfer {

    // Shared mutex protecting the entire banking critical section
    static Semaphore mutex = new Semaphore(1);

    static void transfer(Account from, Account to, int amount) {
        try {
            mutex.acquire();

            try {
                // Simulate processing time
                Thread.sleep(200);

                from.balance -= amount;
                to.balance += amount;

                System.out.println(
                        Thread.currentThread().getName()
                        + " transferred $" + amount
                        + " from " + from.name
                        + " to " + to.name
                );

            } finally {
                // Always release the mutex
                mutex.release();
            }

        } catch (InterruptedException e) {
            System.out.println(
                    Thread.currentThread().getName()
                    + " was interrupted."
            );
            Thread.currentThread().interrupt();
        }
    }
}

public class DeadlockFixed {

    public static void main(String[] args) {

        Account accountA = new Account("A", 1000);
        Account accountB = new Account("B", 1000);

        int startingTotal = accountA.balance + accountB.balance;

        System.out.println("=== Starting Balances ===");
        System.out.println("A: $" + accountA.balance);
        System.out.println("B: $" + accountB.balance);
        System.out.println("Starting total: " + startingTotal);
        System.out.println();

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

        System.out.println();
        System.out.println("=== Final Balances ===");
        System.out.println("Final A: " + accountA.balance);
        System.out.println("Final B: " + accountB.balance);
        System.out.println("Final total: " + finalTotal);

        if (startingTotal == finalTotal) {
            System.out.println("Total balance preserved.");
        } else {
            System.out.println("ERROR: Total balance changed!");
        }

        System.out.println("No deadlock occurred.");
    }
}