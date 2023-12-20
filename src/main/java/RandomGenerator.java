import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * The type Random generator. Generates and returns 1 random
 * number each.
 */
public class RandomGenerator {
    private Set<Integer> hashSet = new LinkedHashSet<>();
    private Iterator<Integer> iterator;
    private Random random = new Random();

    /**
     * Instantiates a new Random generator.
     */
    public RandomGenerator(){
        while (hashSet.size()<90){
            hashSet.add(random.nextInt(1,91));
        }
        iterator = hashSet.iterator();
    }

    /**
     * Get next barrel integer.
     *
     * @return the integer
     */
    public Integer getNextBarrel(){
        return iterator.hasNext() ? iterator.next() : null;
    }
}
