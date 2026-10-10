package Thread_All_Methods;

public class ThreadAllMethods {
    public static void main(String[] args) throws Exception {

        System.out.println("All Methods Test of Thread Class\n");

        // 1. currentThread()
        // Static method - Returns current thread
        System.out.println("--- 1. currentThread() ---");
        Thread main = Thread.currentThread();
        System.out.println("Current thread: " + main);
        System.out.println();


        // 2. getName() & setName()
        // Get/Set Thread name
        System.out.println("--- 2. getName() & setName() ---");
        Thread t1 = new Thread(() -> {
            System.out.println("Running thread name: " + Thread.currentThread().getName());
        });
        t1.setName("MyThread-1");
        System.out.println("Name set to: " + t1.getName());
        t1.start();
        t1.join();
        System.out.println();


        // 3. getPriority() & setPriority()
        // Priority: 1 (MIN) to 10 (MAX), default 5 (NORM)
        System.out.println("--- 3. getPriority() & setPriority() ---");
        Thread t2 = new Thread(() -> {
            System.out.println("Thread priority inside: " + Thread.currentThread().getPriority());
        });
        t2.setPriority(Thread.MAX_PRIORITY);  // 10
        System.out.println("Priority set: " + t2.getPriority());
        System.out.println("MIN=" + Thread.MIN_PRIORITY + ", NORM=" + Thread.NORM_PRIORITY + ", MAX=" + Thread.MAX_PRIORITY);
        t2.start();
        t2.join();
        System.out.println();


        // 4. isDaemon() & setDaemon()
        // Daemon thread = background thread, Stop automatically when JVM exit
        System.out.println("--- 4. isDaemon() & setDaemon() ---");
        Thread t3 = new Thread(() -> {
            System.out.println("Am I daemon? " + Thread.currentThread().isDaemon());
        });
        t3.setDaemon(true);  // must set BEFORE start()
        System.out.println("isDaemon before start: " + t3.isDaemon());
        t3.start();
        t3.join();
        System.out.println();


        // 5. start(), run(), isAlive()
        // start() creates new thread, run() execute the same thread
        System.out.println("--- 5. start(), run(), isAlive() ---");
        Thread t4 = new Thread(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) { }
            System.out.println("t4 task done");
        });
        System.out.println("isAlive before start: " + t4.isAlive());   // false
        t4.start();
        System.out.println("isAlive after start : " + t4.isAlive());   // true
        t4.join();
        System.out.println("isAlive after join  : " + t4.isAlive());   // false
        System.out.println();


        // 6. sleep()
        // Pause current thread for some time
        System.out.println("--- 6. sleep(millis) ---");
        System.out.println("Sleeping 1 second...");
        long before = System.currentTimeMillis();
        Thread.sleep(1000);
        long after = System.currentTimeMillis();
        System.out.println("Slept for: " + (after - before) + "ms");
        System.out.println();


        // 7. join() & join(millis)
        // join() - Wait until thread is finish
        // join(millis) - Wait max N ms, then continue
        System.out.println("--- 7. join() & join(millis) ---");
        Thread t5 = new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException e) { }
            System.out.println("t5 finished");
        });
        t5.start();
        System.out.println("Waiting max 1000ms for t5...");
        t5.join(1000);  // max 1 second wait
        System.out.println("After join(1000)");
        System.out.println();


        // 8. interrupt(), isInterrupted(), interrupted()
        // interrupt() - sends an interrupt signal to the thread
        // isInterrupted() - instance method, check flag (Does not clear)
        // interrupted() - static method, check flag and clear
        System.out.println("--- 8. interrupt(), isInterrupted(), interrupted() ---");
        Thread t6 = new Thread(() -> {
            try {
                System.out.println("t6: sleeping...");
                Thread.sleep(5000);  // 5 second sleep
            } catch (InterruptedException e) {
                System.out.println("t6: interrupted during sleep!");
            }
            // If InterruptedException catch, then flag auto clear
            System.out.println("t6: isInterrupted after catch: " + Thread.currentThread().isInterrupted());
        });
        t6.start();
        Thread.sleep(100);       // main thread 100ms wait
        System.out.println("Interrupting t6...");
        t6.interrupt();
        System.out.println("isInterrupted (from main): " + t6.isInterrupted());
        t6.join();
        System.out.println();


        // 9. yield()
        // Current thread gives up the CPU, allowing other threads a chance to run (hint only, not a guarantee)
        System.out.println("--- 9. yield() ---");
        Thread t7 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("t7: step " + i);
                Thread.yield();
            }
        });
        Thread t8 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("t8: step " + i);
                Thread.yield();
            }
        });
        t7.start();
        t8.start();
        t7.join();
        t8.join();
        System.out.println();


        // 10. getId() & getState()
        // getId() - returns unique thread ID
        // getState() - NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
        System.out.println("--- 10. getId() & getState() ---");
        Thread t9 = new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException e) { }
        });
        System.out.println("State: NEW       -> " + t9.getState());
        t9.start();
        System.out.println("State: RUNNABLE  -> " + t9.getState());
        System.out.println("Thread ID        -> " + t9.getId());
        t9.join();
        System.out.println("State: TERMINATED-> " + t9.getState());
        System.out.println();


        // 11. activeCount() & enumerate()
        // activeCount() - currently active thread count
        // enumerate() - Sends all active threads into the given array
        System.out.println("--- 11. activeCount() & enumerate() ---");
        System.out.println("Active thread count: " + Thread.activeCount());
        Thread[] threadArr = new Thread[Thread.activeCount()];
        Thread.enumerate(threadArr);
        for (Thread t : threadArr) {
            if (t != null) System.out.println("  -> " + t.getName());
        }
        System.out.println();


        // 12. getThreadGroup()
        // Which thread belongs to a which ThreadGroup
        System.out.println("--- 12. getThreadGroup() ---");
        Thread t10 = new Thread(() -> {});
        t10.start();
        System.out.println("Thread group: " + t10.getThreadGroup().getName());
        t10.join();
        System.out.println();


        // 13. getStackTrace()
        // returns current stack trace of a Thread
        System.out.println("--- 13. getStackTrace() ---");
        Thread t11 = new Thread(() -> {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            System.out.println("Stack trace of t11:");
            for (StackTraceElement el : stack) {
                System.out.println("   " + el);
            }
        });
        t11.start();
        t11.join();
        System.out.println();


        // 14. dumpStack()
        // Print Current thread's stack trace in stderr
        System.out.println("--- 14. dumpStack() ---");
        Thread.dumpStack();
        System.out.println();


        // 15. getAllStackTraces()
        // Trace all live thread's stack together
        System.out.println("--- 15. getAllStackTraces() ---");
        java.util.Map<Thread, StackTraceElement[]> allTraces = Thread.getAllStackTraces();
        System.out.println("Total threads alive: " + allTraces.size());
        for (Thread t : allTraces.keySet()) {
            System.out.println("  Thread: " + t.getName());
        }
        System.out.println();


        // 16. holdsLock()
        // Static method - Checks whether the current thread holds the lock on a given object
        System.out.println("--- 16. holdsLock() ---");
        Object lock = new Object();
        System.out.println("Before sync - holdsLock: " + Thread.holdsLock(lock));
        synchronized (lock) {
            System.out.println("Inside sync - holdsLock: " + Thread.holdsLock(lock));
        }
        System.out.println("After sync  - holdsLock: " + Thread.holdsLock(lock));
        System.out.println();


        // 17. setUncaughtExceptionHandler() & getUncaughtExceptionHandler()
        // Defines the handler to be called when a thread throws an unhandled exception
        System.out.println("--- 17. setUncaughtExceptionHandler() ---");
        Thread t12 = new Thread(() -> {
            throw new RuntimeException("Intentional crash!");
        });
        t12.setUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("Caught! Thread: " + thread.getName() + ", Error: " + ex.getMessage());
        });
        System.out.println("Handler: " + t12.getUncaughtExceptionHandler());
        t12.start();
        t12.join();
        System.out.println();


        // 18. setDefaultUncaughtExceptionHandler()
        // Sets a default uncaught exception handler for all threads
        System.out.println("--- 18. setDefaultUncaughtExceptionHandler() ---");
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("[DEFAULT] Thread: " + thread.getName() + " crashed: " + ex.getMessage());
        });
        Thread t13 = new Thread(() -> {
            throw new RuntimeException("Default handler test");
        });
        t13.start();
        t13.join();
        System.out.println("Default handler: " + Thread.getDefaultUncaughtExceptionHandler());
        System.out.println();


        // 19. checkAccess()
        // Checks if the current thread has permission to modify this thread via SecurityManager (mostly a no-op in modern JVMs)
        System.out.println("--- 19. checkAccess() ---");
        Thread t14 = new Thread(() -> {});
        t14.checkAccess();
        System.out.println("checkAccess() passed - no security violation");
        System.out.println();


        // 20. getContextClassLoader() & setContextClassLoader()
        // Gets or sets the context ClassLoader for this thread
        System.out.println("--- 20. getContextClassLoader() & setContextClassLoader() ---");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        System.out.println("Context ClassLoader: " + cl);
        Thread.currentThread().setContextClassLoader(cl);  // same set back
        System.out.println("ClassLoader set back successfully");
        System.out.println();


        // 21. toString()
        // Thread info in string: name, priority, group
        System.out.println("--- 21. toString() ---");
        Thread t15 = new Thread(() -> {});
        t15.setName("LastThread");
        System.out.println("toString: " + t15.toString());
        System.out.println();


        // ==================== DEPRECATED (Don't use korbo, only for known) ====================
        System.out.println("--- DEPRECATED Methods (DO NOT USE) ---");
        System.out.println("  stop()           - unsafe, forcibly stop thread");
        System.out.println("  suspend()        - deadlock occurs, so deprecated");
        System.out.println("  resume()         - pair of  suspend()");
        System.out.println("  countStackFrames()- use with suspend(), so gone");

    }
}