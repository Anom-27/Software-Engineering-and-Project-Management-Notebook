package Static_Vs_NonStatic;

public class StaticVsNonStatic {
    static volatile long staticCount = 0;
    volatile long nonStaticCount = 0;
    static volatile boolean running = true;

    public static void main(String[] args) throws Exception {

        // For 1 minute
        runCase(1, 1);

        // For 5 minutes
        // runCase(2, 5);

        // For 15 minutes
        // runCase(3, 15);
    }

    static void runCase(int caseNo, int min) throws Exception {

        staticCount = 0;
        running = true;
        StaticVsNonStatic obj = new StaticVsNonStatic();

        Thread t1 = new Thread(() -> {
            while (running) {
                staticCount++;
            }
        });

        Thread t2 = new Thread(() -> {
            while (running) {
                obj.nonStaticCount++;
            }
        });

        t1.start();
        t2.start();

        int seconds = min * 60 * 1000;
        Thread.sleep(seconds);

        running = false;

        t1.join();
        t2.join();


        long s = staticCount;
        long n = obj.nonStaticCount;
        double diff = (s - n) * 100.0 / s;

        System.out.println("Case " + caseNo + ": " + min + " min");
        System.out.println("  Static count     : " + s);
        System.out.println("  Non-static count : " + n);
        System.out.printf ("  Non-static slow : %.4f%%%n", diff);
        System.out.println();
    }
}