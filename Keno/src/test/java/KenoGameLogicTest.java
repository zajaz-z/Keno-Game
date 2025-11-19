import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class KenoGameLogicTest {

    @Test
    void testDrawGenerates20UniqueNumbers() {
        KenoGameLogic logic = new KenoGameLogic();
        List<Integer> draw = logic.drawNumbers();
        assertEquals(20, draw.size(), "Should generate 20 numbers");
        assertEquals(20, new HashSet<>(draw).size(), "Numbers must be unique");
    }

    @Test
    void testMatchCountCorrectness() {
        KenoGameLogic logic = new KenoGameLogic();
        Set<Integer> player = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> draw = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10);

        int matches = logic.countMatches(player, draw);
        assertEquals(3, matches, "Should count 3 matching numbers");
    }

    @Test
    void testMatchCountWithNoOverlap() {
        KenoGameLogic logic = new KenoGameLogic();
        Set<Integer> player = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> draw = Arrays.asList(10, 20, 30, 40, 50);
        assertEquals(0, logic.countMatches(player, draw), "No overlaps → 0 matches");
    }

    @Test
    void testRandomDrawRangeIsValid() {
        KenoGameLogic logic = new KenoGameLogic();
        List<Integer> draw = logic.drawNumbers();
        assertTrue(draw.stream().allMatch(n -> n >= 1 && n <= 80), "All numbers must be 1–80");
    }

    @Test
    void testRandomnessBetweenDraws() {
        KenoGameLogic logic = new KenoGameLogic();
        List<Integer> draw1 = logic.drawNumbers();
        List<Integer> draw2 = logic.drawNumbers();
        // Usually they differ, so not equal
        assertNotEquals(draw1, draw2, "Consecutive draws should differ most of the time");
    }
}
