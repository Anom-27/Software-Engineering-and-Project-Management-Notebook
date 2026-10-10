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

## ▶️ How to Run with CMD

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


## ▶️ How to Run with CMD

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
