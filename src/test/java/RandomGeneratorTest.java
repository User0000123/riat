import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @org.junit.jupiter.api.Test
    void getNextBarrel() {
        Set<Integer> hashSet = new LinkedHashSet<>();
        RandomGenerator generator = new RandomGenerator();

        for (int i = 0; i < 90; i++)
        {
            hashSet.add(generator.getNextBarrel());
        }

        assertEquals(90, hashSet.size());
    }
}