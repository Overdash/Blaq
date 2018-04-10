package blaq.tools;

import org.jetbrains.annotations.NotNull;
import blaq.util.CloseableIterator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;

import static blaq.tools.ExceptionHandles.unchecked;
import static blaq.tools.Yield.Completed.completed;
import static blaq.tools.Yield.FlowControl.proceed;
import static blaq.tools.Yield.IfAbsent.ifAb;
import static blaq.tools.Yield.Message.message;

/**
 * Threading implementation of C#'s yield-break and yield-return functionality.
 * Able to produce generators with this class. Values are generated one at a time rather than at once on execution.
 * Alternatively, this could've been implemented with some bytecode or compiler transformations.
 * <p>
 *     When collecting and returning results it's necessary (for efficiency) for the calling code to be able to use
 *     the collected result immediately rather than first waiting for all the results to be collected and stored.
 *     Ultimately this class achieves the following goals:
 *
 *     - Results do not incur overhead of being stored into a list.
 *     - Results can be returned to the user straight away, as they are calculated.
 *     - The calling code is able to abort the collecting process part way through the result collection,
 *       based on its own logic (e.g. if it has enough results before all the results are collected).
 *
 *     This interface allows both the caller and the collector to have their own machine state and call stack control
 *     (also known as the "flow"), through the entire collecting and processing operation.
 * </p>
 *
 * Credit where credit is due: Jim Blackler and Benjamin Weber
 * @param <T>
 */
public interface Yield<T> extends Iterable<T> {

    void execute(YieldDef<T> builder);

    /**
     * Iterators are created on demand by this interface. Whenever an iterator is requested a new thread
     * is created for the collection.
     * @return
     */
    @NotNull
    default CloseableIterator<T> iterator(){ // Originally returned CloseableIterator<T> -  Can change to Iterable<T>
        YieldDef<T> yieldDef = new YieldDef<>();
        Thread collector = new Thread(() -> {
            yieldDef.waitUntilFirstValueRequested();
            try {
                execute(yieldDef);
            } catch (BreakException e){
                //Might add logger here to experiment.
//                System.out.println("broke");
            }
            yieldDef.signalComplete();
        });
        collector.setDaemon(true);
        collector.start();
        yieldDef.onClose(collector::interrupt);
        return yieldDef.iterator();
    }

    /**
     * Nested class defining the properties and behaviors of the yield.
     * Threads are resources, therefore each yield must be disposed of (closed) to free threads.
     *
     * <p>
     *     This class is used to allow the collecting code to have its own call stack, separate from
     *     the calling code; this is achieved using threads. In Java threads get their own private JVM stack,
     *     which is created at the same time as the thread.
     *     {@code YieldDef} uses {@link SynchronousQueue}, which is designed to allow two threads to pass values
     *     between one another and in turn yield control to each other.
     *     Results are calculated and wrapped around a {@link Message} object.
     * </p>
     * @param <T>
     */
    class YieldDef<T> implements Iterable<T>, CloseableIterator<T> {

        private final SynchronousQueue<Message<T>> dataChannel = new SynchronousQueue<>();
        // FlowChannel is used to ensure both threads don't run at the same time
        private final SynchronousQueue<FlowControl> flowChannel = new SynchronousQueue<>();
        private final AtomicReference<Optional<T>> currentValue = new AtomicReference<>(Optional.empty());
        private List<Runnable> toTunOnClose = new CopyOnWriteArrayList<>();

        @NotNull
        @Override
        public CloseableIterator<T> iterator() {
            return this;
        }

        @Override
        public void close() {
//            System.out.println("Closing " + this); // -> Remove this
            toTunOnClose.forEach(Runnable::run);
        }

        @Override
        public boolean hasNext() {
            calculateNextVal();
            Message<T> message = unchecked(dataChannel::take);
            if(message instanceof Completed){
                close();
                return false;
            }
            currentValue.set(message.value());
            return true;
        }

        @Override
        public T next() {
            try {
                ifAb(currentValue.get()).then(this::hasNext);
                return currentValue.get().get();
            } finally {
                currentValue.set(Optional.empty());
            }
        }

        public void returning(T val){
            publish(val);
            waitUntilNextValueRequested();
        }

        /**
         * Wraps the returning value in a message object
         * @param val
         */
        private void publish(T val){
            unchecked(() -> dataChannel.put(message(val)));
        }

        /**
         * Wait for permission to take the first value.
         */
        private void waitUntilFirstValueRequested(){
            waitUntilNextValueRequested();
        }

        /**
         * Wait for permission to continue
         */
        private void waitUntilNextValueRequested(){
            unchecked(flowChannel::take);
        }

        /**
         *  Allow the other thread to gather the result.
         */
        private void calculateNextVal(){
            unchecked(() -> flowChannel.put(proceed));
        }

        public void breaking(){
            throw new BreakException();
        }

        /**
         * Signal no more results left
         */
        public void signalComplete(){
            unchecked(() -> this.dataChannel.put(completed()));
        }

        @Override
        protected void finalize() throws Throwable{
            close();
            super.finalize();
        }

        public void onClose(Runnable onClose){
            this.toTunOnClose.add(onClose);
        }
    }

    class BreakException extends RuntimeException {
        public synchronized Throwable fillStackTrace(){
            return null;
        }
    }

    class IfAbsent {
        public static <T> Then<T> ifAb(Optional<T> opt){// Poor use to Optional, I know.
            return runnable -> {
                if(!opt.isPresent()) runnable.run();
            };
        }
    }

    interface Message<T> {
        Optional<T> value();
        static <T> Message<T> message(T val){
            return () -> Optional.of(val);
        }
    }

    interface Completed<T> extends Message<T> {
        static <T> Completed<T> completed() {
            return Optional::empty;
        }
    }

    interface FlowControl {
        FlowControl proceed = new FlowControl() {};
    }

    interface Then<T> {
        void then(Runnable r);
    }
}