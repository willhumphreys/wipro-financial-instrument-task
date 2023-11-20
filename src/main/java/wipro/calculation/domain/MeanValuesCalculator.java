package wipro.calculation.domain;

import wipro.calculation.service.CalculationService;

/**
 * A calculator for computing the mean value of a financial instrument.
 */
public class MeanValuesCalculator implements Calculator {
    private final InstrumentCalculationRule instrumentCalculationRule;
    private long sum;
    private int count;

    public MeanValuesCalculator(InstrumentCalculationRule instrumentCalculationRules) {
        this.instrumentCalculationRule = instrumentCalculationRules;
    }

    @Override
    public void update(CalculationService.LineData lineData) {
        if (instrumentCalculationRule.test(lineData.instrument(), lineData.date())) {
            long value = (long) (lineData.value() * instrumentCalculationRule.scaleFactor());
            sum += value;
            count++;
        }
    }

    @Override
    public double calculate() {
        return (double) sum / count / instrumentCalculationRule.scaleFactor();
    }

    public String getName() {
        return instrumentCalculationRule.identifier();
    }
}
