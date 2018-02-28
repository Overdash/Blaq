package playground.test;

import org.junit.Test;
import blaq.util.BlaqList;

import java.util.Arrays;

//TODO
public class SelectManyTest {

    @Test
    public void simpleFlatten(){
        BlaqList<Integer> b = new BlaqList<>((Iterable<Integer>)Arrays.asList(3, 5, 20, 15));
        System.out.println(b);
        /*Assert.assertEquals(Arrays.asList('3', '5', '2', '0', '1', '5').toArray(),
                b.projectMany(x->
                        Arrays.asList(x.toString().toCharArray()))
                        .toList().toArray());*/

    }
}
