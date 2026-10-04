package race_condition;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CheckThreads t1 = new CheckThreads();
        CheckThreads t2 = new CheckThreads();
        CheckThreads t3 = new CheckThreads();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Expected count = " + 3 * 100000);
        System.out.println("Actual count = " + CheckThreads.count);
    }
}