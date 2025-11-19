
public class PayoutTable {
//depending on how many matching and how many selections
    //return the amount of money won
    public double calculateWinnings(int spots, int matches) {
        if (spots == 1 && matches == 1) return 2;
        if (spots == 4) {
            return switch (matches) {
                case 2 -> 1;
                case 3 -> 5;
                case 4 -> 75;
                default -> 0;
            };
        }
        if (spots == 8) {
            return switch (matches) {
                case 4 -> 2;
                case 5 -> 20;
                case 6 -> 100;
                case 7 -> 1000;
                case 8 -> 10000;
                default -> 0;
            };
        }
        if (spots == 10) {
            return switch (matches) {
                case 5 -> 2;
                case 6 -> 15;
                case 7 -> 100;
                case 8 -> 500;
                case 9 -> 2000;
                case 10 -> 10000;
                default -> 0;
            };
        }
        return 0;
    }
}
