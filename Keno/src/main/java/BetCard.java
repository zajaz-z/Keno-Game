
import java.util.*;

public class BetCard {
    private final Set<Integer> selectedNumbers = new HashSet<>();
    private int maxSpots = 0;

    public void setMaxSpots(int spots) {
        maxSpots = spots;
        selectedNumbers.clear();
    }

    public boolean selectNumber(int n) {
        if (maxSpots == 0) return false;

        if (selectedNumbers.contains(n)) {
            selectedNumbers.remove(n);
            return true;
        }
        if (selectedNumbers.size() < maxSpots) {
            selectedNumbers.add(n);
            return true;
        }
        return false;
    }

    public void autoPick() {
        selectedNumbers.clear();
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 80; i++) nums.add(i);
        Collections.shuffle(nums);
        selectedNumbers.addAll(nums.subList(0, maxSpots));
    }

    public Set<Integer> getSelectedNumbers() {
        return Collections.unmodifiableSet(selectedNumbers);
    }
}
