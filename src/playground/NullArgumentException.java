package playground;

/**
 * Thrown when an argument to a method is null.
 *
 * @since BLAQ v0
 */
public class NullArgumentException extends RuntimeException {

    NullArgumentException(String msg){
        super(msg);
    }
}
