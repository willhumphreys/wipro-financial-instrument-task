package wipro.calculation.domain;

import wipro.calculation.service.CalculationService;
import wipro.helpers.LexicalDateStringComparator;

import java.util.TreeMap;

/**
 * A calculator for computing the sum of the latest values of a financial instrument.
 */
public class LatestValuesCalculator implements Calculator {

    private final TreeMap<String, Long> latestValuesMap;
    private final int maxEntries;
    private final InstrumentCalculationRule instrumentCalculationRule;

    public LatestValuesCalculator(InstrumentCalculationRule instrumentCalculationRule, LexicalDateStringComparator dateComparator, int maxEntries) {
        this.instrumentCalculationRule = instrumentCalculationRule;

        latestValuesMap = new TreeMap<>(dateComparator);

        this.maxEntries = maxEntries;
    }

    public void update(CalculationService.LineData lineData) {

        long scaleFactor = instrumentCalculationRule.scaleFactor();
        long value = Math.round(lineData.value() * scaleFactor);

        latestValuesMap.put(lineData.date(), value);
        if (latestValuesMap.size() > maxEntries) {
            latestValuesMap.pollFirstEntry(); // Keep only the newest 10 entries
        }
    }


    @Override
    public double calculate() {
        long sum = latestValuesMap.values()
                                  .stream()
                                  .mapToLong(Long::longValue)
                                  .sum();


        return (double) sum / instrumentCalculationRule.scaleFactor();
    }

    public String getName() {
        return instrumentCalculationRule.identifier();
    }
}
