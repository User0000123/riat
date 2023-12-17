import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class RandomGenerator {
    private Set<Integer> hashSet = new LinkedHashSet<>();
    private Iterator<Integer> iterator;
    private Random random = new Random();

    public RandomGenerator(){
        while (hashSet.size()<90){
            hashSet.add(random.nextInt(1,91));
        }
        iterator = hashSet.iterator();
    }

    public Integer getNextBarrel(){
        return iterator.hasNext() ? iterator.next() : null;
    }
}
