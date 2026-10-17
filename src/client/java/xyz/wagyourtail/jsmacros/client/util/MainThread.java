package xyz.wagyourtail.jsmacros.client.util;

import net.minecraft.client.Minecraft;
import xyz.wagyourtail.jsmacros.client.JsMacrosClient;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * General util to run joining with the minecraft main thread; takes care of using semaphor synchronization.
 */
public class MainThread {

    public static void async(Runnable action) {
        run(action, false);
    }

    public static void await(Runnable action) {
        run(action, true);
    }

    public static void run(Runnable action, boolean await) {
        Minecraft minecraft = Minecraft.getInstance();
        // We're in the main thread already, just run directly
        if (JsMacrosClient.clientCore.profile.checkJoinedThreadStack()) {
            action.run();
            return;
        }
        // No awaiting, let it run on its own async
        if (!await) {
            minecraft.execute(() -> {
                try {
                    action.run();
                } catch (Throwable t) {
                    if (!(t.getCause() instanceof InterruptedException)) {
                        JsMacrosClient.clientCore.profile.logError(t);
                    }
                }
            });
            return;
        }

        AtomicReference<Throwable> failure = new AtomicReference<>();
        Semaphore semaphore = new Semaphore(0);
        try {
            minecraft.execute(() -> {
                try {
                    action.run();
                } catch (Throwable t) {
                    failure.set(t);
                } finally {
                    semaphore.release();
                }
            });
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the Minecraft thread", e);
        }

        Throwable t = failure.get();
        if (t instanceof RuntimeException rt) throw rt;
        if (t instanceof Error err) throw err;
        if (t != null) throw new RuntimeException("Main-thread joined operation failed", t);
    }

    public static <T> T await(Supplier<T> action) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isSameThread()) {
            return action.get();
        }
        if (JsMacrosClient.clientCore.profile.checkJoinedThreadStack()) {
            throw new IllegalThreadStateException("Attempted to wait on the Minecraft thread while currently joined to it");
        }
        AtomicReference<T> success = new AtomicReference<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Semaphore semaphore = new Semaphore(0);
        try {
            minecraft.execute(() -> {
                try {
                    success.set(action.get());
                } catch (Throwable t) {
                    failure.set(t);
                } finally {
                    semaphore.release();
                }
            });
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the Minecraft thread", e);
        }

        Throwable t = failure.get();
        if (t instanceof RuntimeException rt) throw rt;
        if (t instanceof Error err) throw err;
        if (t != null) throw new RuntimeException("Main-thread joined operation failed", t);
        return success.get();
    }

}
