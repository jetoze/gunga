package jetoze.gunga;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.*;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

/**
 * Utilities related to the Event Dispatching Thread, referred to here as the "UI thread", 
 * for brevity.
 * <p>
 * Some of these utilities are simple wrappers around existing methods in {@link SwingUtilities}
 * and {@link EventQueue}, but with slightly more direct names.
 * <p>
 * Unless otherwise stated, all methods in this class rejects {@code null} input arguments
 * by throwing a {@code NullPointerException}.
 * 
 */
public final class UiThread {

    /**
     * Checks if the current thread is the UI thread.
     */
    public static boolean isUiThread() {
        return EventQueue.isDispatchThread();
    }
    
    /**
     * Calls {@code job.run()} if the calling thread is the UI thread, otherwise
     * passes the job to {@link #runLater(Runnable)}.
     */
    public static void run(Runnable job) {
        requireNonNull(job);
        if (isUiThread()) {
            job.run();
        } else {
            runLater(job);
        }
    }

    /**
     * Enqueues {@code job} on the event dispatching thread, for later execution.
     */
    public static void runLater(Runnable job) {
        requireNonNull(job);
        EventQueue.invokeLater(job);
    }
    
    /**
     * Throws an IllegalStateException without a message if the current thread is
     * not the UI thread.
     */
    public static void throwIfNotUiThread() {
        checkState(isUiThread());
    }
    
    /**
     * Throws an IllegalStateException with the given message if the current thread is
     * not the UI thread.
     * 
     * @throws IllegalArgumentException if message is blank
     */
    public static void throwIfNotUiThread(String message) {
        checkArgument(!message.isBlank(), "message cannot be blank");
        checkState(isUiThread(), message);
    }
    
    private UiThread() {/**/}
}
