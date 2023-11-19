package underover.game;
public enum BetRatio {
    FOUR(4, 0.8, 0.2),
    FIVE(5, 0.7, 0.3),
    SIX(6, 0.6, 0.4),
    SEVEN(7, 0.5, 0.5),
    EIGHT(8, 0.4, 0.6),
    NINE(9, 0.3, 0.7);

    private final int id;
    private final double lowerRatio;
    private final double higherRatio;

    BetRatio(int id, double lowerRatio, double higherRatio) {
        this.id = id;
        this.lowerRatio = lowerRatio;
        this.higherRatio = higherRatio;
    }

    public int getId() {
        return id;
    }

    public double getLowerRatio() {
        return lowerRatio;
    }

    public double getHigherRatio() {
        return higherRatio;
    }
}