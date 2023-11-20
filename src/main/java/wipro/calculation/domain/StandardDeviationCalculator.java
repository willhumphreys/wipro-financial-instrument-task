package wipro.calculation.domain;

import wipro.calculation.service.CalculationService;

/**
 * A calculator for computing the standard deviation of a financial instrument's values.
 */
public class StandardDeviationCalculator implements Calculator {
    private final InstrumentCalculationRule instrumentCalculationRule;
    private long sum;
    private int count;
    private double squareSum;

    public StandardDeviationCalculator(InstrumentCalculationRule instrumentCalculationRule) {
        this.instrumentCalculationRule = instrumentCalculationRule;
    }

    public void update(CalculationService.LineData lineData) {

        if (instrumentCalculationRule.test(lineData.instrument(), lineData.date())) {
            long value = Math.round(lineData.value() * instrumentCalculationRule.scaleFactor());
            sum += value;
            squareSum += (double) value * value; // Consider potential overflow
            count++;
        }
    }

    public double calculate() {
        if (count > 1) {
            double mean = (double) sum / count;
            double variance = (squareSum / count) - (mean * mean);
            double scaleFactor = instrumentCalculationRule.scaleFactor();
            variance /= (scaleFactor * scaleFactor);

            return Math.sqrt(variance);
        }
        return 0.0;
    }

    public String getName() {
        return instrumentCalculationRule.identifier();
    }
}
