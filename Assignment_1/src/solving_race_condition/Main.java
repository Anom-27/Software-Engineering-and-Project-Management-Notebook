package solving_race_condition;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        T1 t1 = new T1();
        T1 t2 = new T1();
        T1 t3 = new T1();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Expected count = " + 3 * 100000);
        System.out.println("Actual count = " + T1.count);

        T2 t4 = new T2();
        T2 t5 = new T2();
        T2 t6 = new T2();

        t4.start();
        t5.start();
        t6.start();

        t4.join();
        t5.join();
        t6.join();

        System.out.println("Expected count = " + 3 * 100000);
        System.out.println("Actual count = " + T2.count.get());
    }
}
