package blaq.util;

import blaq.annotations.Readonly;

import java.util.Comparator;

/**
 * Representation of a composite key type
 * @param <T> Primary key type
 * @param <S> Secondary key type
 */
class CompositeKey<T, S> {

    @Readonly
    private T primary;
    @Readonly
    private  S secondary;

    CompositeKey(T primary, S secondary){
        this.primary = primary;
        this.secondary = secondary;
    }

    CompositeKey(){} // small hack

    T getPrimary(){
        return primary;
    }

    S getSecondary(){
        return secondary;
    }

    final class CompComparator implements Comparator<CompositeKey<T,S>>{

        @Readonly
        private Comparator<T> primaryComparator;
        @Readonly
        private Comparator<S> secondaryComparator;

        CompComparator(Comparator<T> primaryComparator, Comparator<S> secondaryComparator){
            this.primaryComparator = primaryComparator;
            this.secondaryComparator = secondaryComparator;
        }

        @Override
        public int compare(CompositeKey<T, S> o1, CompositeKey<T, S> o2) {
            int primaryResult = primaryComparator.compare(o1.primary, o2.primary);

            if(primaryResult != 0)
                return primaryResult;

            return secondaryComparator.compare(o1.secondary, o2.secondary);
        }
    }
}
