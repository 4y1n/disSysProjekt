package at.technikum.percentageservice;

public class PercentageCalculator {

    // community_depleted: wie viel % des Community-Pools verbraucht wurde
    public double calculateCommunityDepleted(double produced, double used) {
        return produced > 0 ? (used / produced) * 100.0 : 0.0;
    }

    // grid_portion: wie viel % der Gesamtenergie vom Grid kam
    public double calculateGridPortion(double used, double grid) {
        double total = used + grid;
        return total > 0 ? (grid / total) * 100.0 : 0.0;
    }
}