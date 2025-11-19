
public class Player {
    private double totalWinnings = 0;// keep track of how much won

    public void addWinnings(double amount) {
        totalWinnings += amount;
    }

    public double getTotalWinnings() {
        return totalWinnings;
    }

    public void reset() {
        totalWinnings = 0; // if starting new game
    }
}
