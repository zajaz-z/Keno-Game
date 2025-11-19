import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PayoutTableTest {

    private final PayoutTable payout = new PayoutTable();

    @Test
    void test1SpotWin() {
        assertEquals(2.0, payout.calculateWinnings(1, 1));
        assertEquals(0.0, payout.calculateWinnings(1, 0));
    }

    @Test
    void test4SpotWinnings() {
        assertEquals(1.0, payout.calculateWinnings(4, 2));
        assertEquals(5.0, payout.calculateWinnings(4, 3));
        assertEquals(75.0, payout.calculateWinnings(4, 4));
        assertEquals(0.0, payout.calculateWinnings(4, 1));
    }

    @Test
    void test8SpotWinnings() {
        assertEquals(2.0, payout.calculateWinnings(8, 4));
        assertEquals(20.0, payout.calculateWinnings(8, 5));
        assertEquals(100.0, payout.calculateWinnings(8, 6));
        assertEquals(1000.0, payout.calculateWinnings(8, 7));
        assertEquals(10000.0, payout.calculateWinnings(8, 8));
    }

    @Test
    void test10SpotWinnings() {
        assertEquals(2.0, payout.calculateWinnings(10, 5));
        assertEquals(15.0, payout.calculateWinnings(10, 6));
        assertEquals(100.0, payout.calculateWinnings(10, 7));
        assertEquals(500.0, payout.calculateWinnings(10, 8));
        assertEquals(2000.0, payout.calculateWinnings(10, 9));
        assertEquals(10000.0, payout.calculateWinnings(10, 10));
    }

    @Test
    void testInvalidInputsReturnZero() {
        assertEquals(0.0, payout.calculateWinnings(99, 5));
        assertEquals(0.0, payout.calculateWinnings(4, 10));
        assertEquals(0.0, payout.calculateWinnings(0, 0));
    }
}
