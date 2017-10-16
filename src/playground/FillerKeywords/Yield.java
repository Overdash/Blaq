package playground.FillerKeywords;

import org.jetbrains.annotations.NotNull;
import playground.Collections.ClosableIterator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;

import static playground.FillerKeywords.ExceptionHandles.unchecked;
import static playground.FillerKeywords.Yield.Completed.completed;
import static playground.FillerKeywords.Yield.FlowControl.proceed;
import static playground.FillerKeywords.Yield.IfAbsent.ifAb;
import static playground.FillerKeywords.Yield.Message.message;

/**
 * Threading implementation of C#'s yield-break and yield-return functionality.
 * Able to produce generators with this class. Values are generated one at a time rather than at once on execution.
 * Alternatively, this could've been implemented with some bytecode or compiler transformations.
 * @param <T>
 */
public interface Yield<T> extends Iterable<T> {

    void execute(YieldDef<T> builder);

    @NotNull
    default ClosableIterator<T> iterator(){ // Originally returned ClosableIterator<T> -  Can change to Iterable<T>
        YieldDef<T> yieldDef = new YieldDef<>();
        Thread collector = new Thread(() -> {
            yieldDef.waitUntilFirstValueRequested();
            try {
                execute(yieldDef);
            } catch (BreakException e){
                //Might add logger here to experiment.
                System.out.println("broke");
            }
            yieldDef.signalComplete();
        });
        collector.setDaemon(true);
        collector.start();
        yieldDef.onClose(collector::interrupt);
        return yieldDef.iterator();
    }

    /**
     * Nested class defining the properties and behaviors of the yield. Each yield must be disposed of (closed) to free threads.
     * @param <T>
     */
    class YieldDef<T> implements Iterable<T>, ClosableIterator<T> {

        private final SynchronousQueue<Message<T>> dataChannel = new SynchronousQueue<>();
        private final SynchronousQueue<FlowControl> flowChannel = new SynchronousQueue<>();
        private final AtomicReference<Optional<T>> currentValue = new AtomicReference<>(Optional.empty());
        private List<Runnable> toTunOnClose = new CopyOnWriteArrayList<>();

        @NotNull
        @Override
        public ClosableIterator<T> iterator() {
            return this;
        }

        @Override
        public void close() {
            System.out.println("Closing " + this); // -> Remove this
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

        private void publish(T val){
            unchecked(() -> dataChannel.put(message(val)));
        }

        private void waitUntilFirstValueRequested(){
            waitUntilNextValueRequested();
        }

        private void waitUntilNextValueRequested(){
            unchecked(flowChannel::take);
        }

        private void calculateNextVal(){
            unchecked(() -> flowChannel.put(proceed));
        }

        public void breaking(){
            throw new BreakException();
        }

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