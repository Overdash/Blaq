package blaq.tools;

public class ExceptionHandles {
    public interface ExceptionSupplier<T, E extends Exception>{
        T get() throws E;
    }

    public interface VoidException<E extends Exception>{
        void apply() throws E;
    }

    public static <T, E extends Exception> T unchecked(ExceptionSupplier<T, E> supplier){
        try{
            return supplier.get();
        } catch (Error | RuntimeException re){
            throw re;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void unchecked(VoidException m){
        try{
            m.apply();
        } catch (Error | RuntimeException re){
            throw re;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
