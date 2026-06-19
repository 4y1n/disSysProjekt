package at.technikum.usageservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsageCalculatorTest {

    private UsageCalculator calculator;

    @BeforeEach
    void setUp() { calculator = new UsageCalculator(); }

    @Test
    void testUserMessage_exampleFromSpec() {
        // GEGEBEN: 14:00, produced=18.05, used=18.02, grid=1.056
        HourlyUsageRow row = new HourlyUsageRow(
                LocalDateTime.of(2025, 1, 10, 14, 0), 18.05, 18.02, 1.056);

        EnergyMessage msg = new EnergyMessage();
        msg.setType("USER"); msg.setKwh(0.05);
        msg.setDatetime(LocalDateTime.of(2025, 1, 10, 14, 34));

        // WENN
        calculator.apply(row, msg);

        // DANN: 0.03 aus Community, 0.02 aus Grid
        assertEquals(18.05, row.getCommunityProduced(), 0.0001);
        assertEquals(18.05, row.getCommunityUsed(),     0.0001);
        assertEquals(1.076, row.getGridUsed(),          0.0001);
    }

    @Test
    void testProducerMessage_onlyProducedIncreases() {
        HourlyUsageRow row = new HourlyUsageRow(
                LocalDateTime.of(2025, 1, 10, 14, 0), 10.0, 5.0, 0.5);
        EnergyMessage msg = new EnergyMessage();
        msg.setType("PRODUCER"); msg.setKwh(2.0);
        msg.setDatetime(LocalDateTime.of(2025, 1, 10, 14, 15));

        calculator.apply(row, msg);

        assertEquals(12.0, row.getCommunityProduced(), 0.0001);
        assertEquals(5.0,  row.getCommunityUsed(),     0.0001);
        assertEquals(0.5,  row.getGridUsed(),          0.0001);
    }

    @Test
    void testUserMessage_poolEmpty_allFromGrid() {
        HourlyUsageRow row = new HourlyUsageRow(LocalDateTime.of(2025, 1, 10, 14, 0));
        EnergyMessage msg = new EnergyMessage();
        msg.setType("USER"); msg.setKwh(0.1);
        msg.setDatetime(LocalDateTime.of(2025, 1, 10, 14, 5));

        calculator.apply(row, msg);

        assertEquals(0.0, row.getCommunityProduced(), 0.0001);
        assertEquals(0.0, row.getCommunityUsed(),     0.0001);
        assertEquals(0.1, row.getGridUsed(),          0.0001);
    }

    @Test
    void testUserMessage_poolSufficient_nothingFromGrid() {
        HourlyUsageRow row = new HourlyUsageRow(
                LocalDateTime.of(2025, 1, 10, 14, 0), 5.0, 1.0, 0.0);
        EnergyMessage msg = new EnergyMessage();
        msg.setType("USER"); msg.setKwh(1.0);
        msg.setDatetime(LocalDateTime.of(2025, 1, 10, 14, 10));

        calculator.apply(row, msg);

        assertEquals(5.0, row.getCommunityProduced(), 0.0001);
        assertEquals(2.0, row.getCommunityUsed(),     0.0001);
        assertEquals(0.0, row.getGridUsed(),          0.0001);
    }

    @Test
    void testInvariant_usedNeverExceedsProduced() {
        HourlyUsageRow row = new HourlyUsageRow(
                LocalDateTime.of(2025, 1, 10, 14, 0), 3.0, 3.0, 0.5);
        EnergyMessage msg = new EnergyMessage();
        msg.setType("USER"); msg.setKwh(2.0);
        msg.setDatetime(LocalDateTime.of(2025, 1, 10, 14, 20));

        calculator.apply(row, msg);

        assertTrue(row.getCommunityUsed() <= row.getCommunityProduced(),
                "communityUsed darf communityProduced nie ueberschreiten!");
        assertEquals(2.5, row.getGridUsed(), 0.0001);
    }

    @Test
    void testTruncateToHour() {
        assertEquals(
                LocalDateTime.of(2025, 1, 10, 14, 0, 0),
                calculator.truncateToHour(LocalDateTime.of(2025, 1, 10, 14, 33, 47)));
    }
}