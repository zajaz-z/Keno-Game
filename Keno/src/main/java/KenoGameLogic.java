
import java.util.*;

public class KenoGameLogic {
    private final Random random = new Random();

    /** Generates 20 unique random numbers from 1–80. */
    public List<Integer> drawNumbers() {
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() < 20) {
            numbers.add(random.nextInt(80) + 1);
        }
        return new ArrayList<>(numbers);
    }

    /** Returns the count of matches between player and draw. */
    public int countMatches(Set<Integer> playerNumbers, List<Integer> draw) {
        int count = 0;
        for (int n : playerNumbers) {
            if (draw.contains(n)) count++;
        }
        return count;
    }
}
