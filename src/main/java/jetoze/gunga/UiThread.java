package jetoze.gunga;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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
    
    // TODO: Too many offload options. Introduce some sort of builder for this?
    
    public static <T> void offload(Callable<T> work, Consumer<? super T> consumer) {
        offload(work, consumer, Throwable::printStackTrace);
    }
    
    public static <T> void offload(Callable<T> work, 
                                   Consumer<? super T> consumer,
                                   boolean acceptNullResults) {
        offload(work, consumer, acceptNullResults, Throwable::printStackTrace);
    }

    public static <T> void offload(Callable<T> work, 
                                   Consumer<? super T> consumer,
                                   Consumer<? super Throwable> exceptionHandler) {
        offload(work, consumer, false, exceptionHandler);
    }
    
    public static <T> void offload(Callable<T> work, 
                                   Consumer<? super T> consumer,
                                   boolean acceptNullResults,
                                   Consumer<? super Throwable> exceptionHandler) {
        requireNonNull(work);
        requireNonNull(consumer);
        requireNonNull(exceptionHandler);
        new SwingWorker<T, Void>() {

            @Override
            protected T doInBackground() throws Exception {
                return work.call();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    if (result != null || acceptNullResults) {
                        consumer.accept(result);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    exceptionHandler.accept(e);
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    exceptionHandler.accept(cause);
                }
            }
        }.execute();
    }
    
    public static void offload(Runnable work, Runnable whenDone) {
        offload(work, whenDone, Throwable::printStackTrace);
    }
    
    public static void offload(Runnable work, Runnable whenDone, Consumer<? super Throwable> exceptionHandler) {
        requireNonNull(work);
        requireNonNull(whenDone);
        requireNonNull(exceptionHandler);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                work.run();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    whenDone.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    exceptionHandler.accept(e);
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    exceptionHandler.accept(cause);
                }
            }
        }.execute();
    }
    
    
    private UiThread() {/**/}
}
