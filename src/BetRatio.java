public enum BetRatio {

    FOUR(4, 1.8, 0.2),
    FIVE(5, 1.5, 0.5),
    SIX(6, 1.3, 0.8),
    SEVEN(7, 1, 1),
    EIGHT(8, 0.8, 1.3),
    NINE(9, 0.5, 1.5);

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
