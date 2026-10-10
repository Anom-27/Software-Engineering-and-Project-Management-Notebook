# Java Multithreading — Thread Tasks

This project covers two Java multithreading tasks:

1. **Static vs Non-Static Thread Count** — 3 cases (1 min, 5 min, 15 min)
2. **Thread Class — All Methods Demo**

---

# Task 1 — Static vs Non-Static Thread Count

## 📌 Concept

Two threads run in infinite loops simultaneously.

- `Thread t1` increments a **static** variable (`staticCount`) — class level, shared across all objects
- `Thread t2` increments a **non-static** variable (`nonStaticCount`) — object level, belongs to one instance

After a fixed time, both threads stop. We compare how many times each thread was able to count, and calculate how much the non-static count is **behind** the static count in percentage.

```
Static variable    → class level  → directly accessible
Non-static variable → object level → needs object reference
```

## 🔑 Why `volatile`?

```
static volatile long staticCount = 0;
volatile long nonStaticCount = 0;
static volatile boolean running = true;
```

Without `volatile`, the **JIT compiler** may cache the variable value in the CPU register of each thread. This causes two problems:

| Problem | Effect |
|---|---|
| `staticCount` / `nonStaticCount` without `volatile` | Threads may read stale cached values, not actual memory |
| `running` without `volatile` | `running = false` may never be seen by t1/t2 → **infinite loop / hang** |

`volatile` forces every read/write to go directly to **main memory**, so all threads always see the latest value.

## 📂 File

`StaticVsNonStatic.java`

## 💻 Code

```java
public class StaticVsNonStatic {

    // Static variable - class level
    static volatile long staticCount = 0;

    // Non-static variable - object level
    volatile long nonStaticCount = 0;

    // Running flag - volatile must
    static volatile boolean running = true;

    public static void main(String[] args) throws Exception {

        // ========== CASE 1: 1 minute ==========
        runCase(1, 1);

        // ========== CASE 2: 5 minutes ==========
        // runCase(2, 5);

        // ========== CASE 3: 15 minutes ==========
        // runCase(3, 15);
    }

    static void runCase(int caseNo, int min) throws Exception {

        // Reset koro
        staticCount = 0;
        running = true;
        StaticVsNonStatic obj = new StaticVsNonStatic();

        // Static count thread
        Thread t1 = new Thread(() -> {
            while (running) {
                staticCount++;
            }
        });

        // Non-static count thread
        Thread t2 = new Thread(() -> {
            while (running) {
                obj.nonStaticCount++;
            }
        });

        t1.start();
        t2.start();

        // Wait koro
        int milliseconds = min * 60 * 1000;
        Thread.sleep(milliseconds);

        running = false;

        t1.join();
        t2.join();

        // Result
        long s = staticCount;
        long n = obj.nonStaticCount;
        double diff = (s - n) * 100.0 / s;

        System.out.println("Case " + caseNo + " (" + min + " min)");
        System.out.println("  Static count     : " + s);
        System.out.println("  Non-static count : " + n);
        System.out.printf ("  Non-static pichiye : %.4f%%%n", diff);
        System.out.println();
    }
}
```

## ▶️ How to Run

**Compile:**
```
javac StaticVsNonStatic.java
java StaticVsNonStatic
```

**Switch cases:**

| What to do | How |
|---|---|
| Run Case 1 (1 min) | Keep `runCase(1, 1)` uncommented |
| Run Case 2 (5 min) | Comment Case 1, uncomment `runCase(2, 5)` |
| Run Case 3 (15 min) | Comment Cases 1 & 2, uncomment `runCase(3, 15)` |
| Run all 3 | Uncomment all 3 lines (total 21 min) |

## 📊 Expected Output

```
Case 1 (1 min)
  Static count     : 1873562841
  Non-static count : 1869234512
  Non-static pichiye : 0.2312%

Case 2 (5 min)
  Static count     : 9341782305
  Non-static count : 9318923411
  Non-static pichiye : 0.2447%

Case 3 (15 min)
  Static count     : 28012349871
  Non-static count : 27941234098
  Non-static pichiye : 0.2538%
```

> **Note:** Actual numbers will vary every run. CPU scheduling, JIT optimization, and core count affect results.

## 🧠 Key Takeaways

- Static variable is accessed at class level, non-static needs object reference — access time differs slightly
- `volatile` on `running` is **mandatory**, otherwise threads may hang (never see `false`)
- `volatile` on count variables ensures correct main-memory writes (important for accuracy)
- Result difference is usually very small (~0.2%) because both are simple field increments in JVM
- Result will **not be identical** across runs — that is expected behavior

---

# Task 2 — Thread Class All Methods Demo

## 📌 Concept

Java's `Thread` class has many built-in methods for managing thread **lifecycle, priority, state, interruption, and debugging**. This program demonstrates all of them in one file, with each method in its own clearly labeled section.

## 📂 File

`ThreadAllMethods.java`

## 📋 All Methods Covered

| # | Method | Type | What it does |
|---|--------|-------|-------------|
| 1 | `currentThread()` | static | Returns the currently running thread |
| 2 | `getName()` / `setName()` | instance | Get or set thread name |
| 3 | `getPriority()` / `setPriority()` | instance | Priority from 1 (MIN) to 10 (MAX) |
| 4 | `isDaemon()` / `setDaemon()` | instance | Background thread flag |
| 5 | `start()` / `run()` / `isAlive()` | instance | Thread lifecycle control |
| 6 | `sleep(millis)` | static | Pause current thread |
| 7 | `join()` / `join(millis)` | instance | Wait for thread to finish |
| 8 | `interrupt()` / `isInterrupted()` / `interrupted()` | both | Send and check interrupt signal |
| 9 | `yield()` | static | Give up CPU to other threads |
| 10 | `getId()` / `getState()` | instance | Unique ID and current state |
| 11 | `activeCount()` / `enumerate()` | static | Count and list active threads |
| 12 | `getThreadGroup()` | instance | Get the thread's group |
| 13 | `getStackTrace()` | instance | Get thread's current call stack |
| 14 | `dumpStack()` | static | Print stack trace to stderr |
| 15 | `getAllStackTraces()` | static | All live threads' stack traces |
| 16 | `holdsLock(obj)` | static | Check if thread holds a lock |
| 17 | `setUncaughtExceptionHandler()` | instance | Handler for unhandled exceptions |
| 18 | `setDefaultUncaughtExceptionHandler()` | static | Global handler for all threads |
| 19 | `checkAccess()` | instance | Security permission check |
| 20 | `getContextClassLoader()` / `setContextClassLoader()` | instance | ClassLoader for the thread |
| 21 | `toString()` | instance | Thread info as string |

## ⚠️ Deprecated Methods (Do NOT use)

| Method | Why deprecated |
|---|---|
| `stop()` | Force-stops thread unsafely, can corrupt shared data |
| `suspend()` | Causes deadlocks, never releases lock |
| `resume()` | Paired with `suspend()`, same problem |
| `countStackFrames()` | Depends on `suspend()`, removed |

## 💻 Code

```java
public class ThreadAllMethods {

    public static void main(String[] args) throws Exception {

        System.out.println("====== Thread Class - All Methods Test ======\n");

        // ==================== 1. currentThread() ====================
        System.out.println("--- 1. currentThread() ---");
        Thread main = Thread.currentThread();
        System.out.println("Current thread: " + main);
        System.out.println();

        // ==================== 2. getName() & setName() ====================
        System.out.println("--- 2. getName() & setName() ---");
        Thread t1 = new Thread(() -> {
            System.out.println("Running thread name: " + Thread.currentThread().getName());
        });
        t1.setName("MyThread-1");
        System.out.println("Name set to: " + t1.getName());
        t1.start();
        t1.join();
        System.out.println();

        // ==================== 3. getPriority() & setPriority() ====================
        System.out.println("--- 3. getPriority() & setPriority() ---");
        Thread t2 = new Thread(() -> {
            System.out.println("Thread priority inside: " + Thread.currentThread().getPriority());
        });
        t2.setPriority(Thread.MAX_PRIORITY);
        System.out.println("Priority set: " + t2.getPriority());
        System.out.println("MIN=" + Thread.MIN_PRIORITY + ", NORM=" + Thread.NORM_PRIORITY + ", MAX=" + Thread.MAX_PRIORITY);
        t2.start();
        t2.join();
        System.out.println();

        // ==================== 4. isDaemon() & setDaemon() ====================
        System.out.println("--- 4. isDaemon() & setDaemon() ---");
        Thread t3 = new Thread(() -> {
            System.out.println("Am I daemon? " + Thread.currentThread().isDaemon());
        });
        t3.setDaemon(true);
        System.out.println("isDaemon before start: " + t3.isDaemon());
        t3.start();
        t3.join();
        System.out.println();

        // ==================== 5. start(), run(), isAlive() ====================
        System.out.println("--- 5. start(), run(), isAlive() ---");
        Thread t4 = new Thread(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) { }
            System.out.println("t4 task done");
        });
        System.out.println("isAlive before start: " + t4.isAlive());
        t4.start();
        System.out.println("isAlive after start : " + t4.isAlive());
        t4.join();
        System.out.println("isAlive after join  : " + t4.isAlive());
        System.out.println();

        // ==================== 6. sleep() ====================
        System.out.println("--- 6. sleep(millis) ---");
        System.out.println("Sleeping 1 second...");
        long before = System.currentTimeMillis();
        Thread.sleep(1000);
        long after = System.currentTimeMillis();
        System.out.println("Slept for: " + (after - before) + "ms");
        System.out.println();

        // ==================== 7. join() & join(millis) ====================
        System.out.println("--- 7. join() & join(millis) ---");
        Thread t5 = new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException e) { }
            System.out.println("t5 finished");
        });
        t5.start();
        System.out.println("Waiting max 1000ms for t5...");
        t5.join(1000);
        System.out.println("After join(1000)");
        System.out.println();

        // ==================== 8. interrupt(), isInterrupted(), interrupted() ====================
        System.out.println("--- 8. interrupt(), isInterrupted(), interrupted() ---");
        Thread t6 = new Thread(() -> {
            try {
                System.out.println("t6: sleeping...");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("t6: interrupted during sleep!");
            }
            System.out.println("t6: isInterrupted after catch: " + Thread.currentThread().isInterrupted());
        });
        t6.start();
        Thread.sleep(100);
        System.out.println("Interrupting t6...");
        t6.interrupt();
        System.out.println("isInterrupted (from main): " + t6.isInterrupted());
        t6.join();
        System.out.println();

        // ==================== 9. yield() ====================
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
        t7.start(); t8.start();
        t7.join();  t8.join();
        System.out.println();

        // ==================== 10. getId() & getState() ====================
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

        // ==================== 11. activeCount() & enumerate() ====================
        System.out.println("--- 11. activeCount() & enumerate() ---");
        System.out.println("Active thread count: " + Thread.activeCount());
        Thread[] threadArr = new Thread[Thread.activeCount()];
        Thread.enumerate(threadArr);
        for (Thread t : threadArr) {
            if (t != null) System.out.println("  -> " + t.getName());
        }
        System.out.println();

        // ==================== 12. getThreadGroup() ====================
        System.out.println("--- 12. getThreadGroup() ---");
        Thread t10 = new Thread(() -> {});
        t10.start();
        System.out.println("Thread group: " + t10.getThreadGroup().getName());
        t10.join();
        System.out.println();

        // ==================== 13. getStackTrace() ====================
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

        // ==================== 14. dumpStack() ====================
        System.out.println("--- 14. dumpStack() ---");
        Thread.dumpStack();
        System.out.println();

        // ==================== 15. getAllStackTraces() ====================
        System.out.println("--- 15. getAllStackTraces() ---");
        java.util.Map<Thread, StackTraceElement[]> allTraces = Thread.getAllStackTraces();
        System.out.println("Total threads alive: " + allTraces.size());
        for (Thread t : allTraces.keySet()) {
            System.out.println("  Thread: " + t.getName());
        }
        System.out.println();

        // ==================== 16. holdsLock() ====================
        System.out.println("--- 16. holdsLock() ---");
        Object lock = new Object();
        System.out.println("Before sync - holdsLock: " + Thread.holdsLock(lock));
        synchronized (lock) {
            System.out.println("Inside sync - holdsLock: " + Thread.holdsLock(lock));
        }
        System.out.println("After sync  - holdsLock: " + Thread.holdsLock(lock));
        System.out.println();

        // ==================== 17. setUncaughtExceptionHandler() ====================
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

        // ==================== 18. setDefaultUncaughtExceptionHandler() ====================
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

        // ==================== 19. checkAccess() ====================
        System.out.println("--- 19. checkAccess() ---");
        Thread t14 = new Thread(() -> {});
        t14.checkAccess();
        System.out.println("checkAccess() passed - no security violation");
        System.out.println();

        // ==================== 20. getContextClassLoader() & setContextClassLoader() ====================
        System.out.println("--- 20. getContextClassLoader() & setContextClassLoader() ---");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        System.out.println("Context ClassLoader: " + cl);
        Thread.currentThread().setContextClassLoader(cl);
        System.out.println("ClassLoader set back successfully");
        System.out.println();

        // ==================== 21. toString() ====================
        System.out.println("--- 21. toString() ---");
        Thread t15 = new Thread(() -> {});
        t15.setName("LastThread");
        System.out.println("toString: " + t15.toString());
        System.out.println();

        System.out.println("====== All Thread Methods Tested ======");
    }
}
```

## ▶️ How to Run

**Compile:**
```
javac ThreadAllMethods.java
java ThreadAllMethods
```

## 🔍 Thread States Explained

```
NEW  ──► RUNNABLE ──► TERMINATED
              │
              ├──► BLOCKED        (waiting for lock)
              ├──► WAITING        (join / wait with no timeout)
              └──► TIMED_WAITING  (sleep / join(ms))
```

| State | When |
|---|---|
| `NEW` | Thread created but `start()` not called |
| `RUNNABLE` | After `start()`, running or ready to run |
| `BLOCKED` | Waiting to enter a `synchronized` block |
| `WAITING` | Waiting indefinitely (`join()`, `wait()`) |
| `TIMED_WAITING` | Waiting with timeout (`sleep(ms)`, `join(ms)`) |
| `TERMINATED` | Thread finished execution |

## 🧠 Key Takeaways

- `start()` creates a new thread; `run()` executes in the **same** thread — do not confuse them
- `interrupt()` only sends a signal; the thread must **check** it via `isInterrupted()` or catch `InterruptedException`
- `isInterrupted()` — checks flag, does **not** clear it
- `interrupted()` — checks flag AND **clears** it (static method)
- `yield()` is only a **hint** to the scheduler, not a guarantee
- `setDaemon(true)` must be called **before** `start()`, otherwise throws `IllegalThreadStateException`
- `volatile` is not the same as `synchronized` — it ensures visibility, not atomicity

---

## ⚖️ `isInterrupted()` vs `interrupted()`

| | `isInterrupted()` | `interrupted()` |
|---|---|---|
| Type | Instance method | Static method |
| Clears flag? | ❌ No | ✅ Yes |
| Usage | Check from outside thread | Check from inside thread |

---

*Java Version: JDK 8+*
