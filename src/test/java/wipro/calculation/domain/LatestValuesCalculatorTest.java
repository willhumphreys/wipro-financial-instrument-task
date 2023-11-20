package wipro.calculation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wipro.calculation.config.LatestValuesCalculatorFactory;
import wipro.calculation.service.CalculationService;

import static org.assertj.core.api.Assertions.assertThat;

public class LatestValuesCalculatorTest {

    public static final long SCALE_FACTOR = 1_000_000L;
    private LatestValuesCalculator calculator;

    @BeforeEach
    public void setUp() {
        InstrumentCalculationRule rule = new InstrumentCalculationRule("INSTRUMENT_TEST", null, SCALE_FACTOR);
        calculator = new LatestValuesCalculator(rule, LatestValuesCalculatorFactory.DATE_COMPARATOR, 3);
    }

    @Test
    public void testUpdateAndCalculate() {

        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 1.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "05-Mar-2002", 2.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "02-Apr-2003", 3.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2004", 4.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2005", 5.0));


        assertThat(calculator.calculate()).isEqualTo(3 + 4 + 5);
    }

    @Test
    public void testWithFewerEntries() {
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 1.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "05-Mar-2002", 2.0));

        assertThat(calculator.calculate()).isEqualTo(1 + 2);
    }

    @Test
    public void testEntriesAddedOutOfOrder() {
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2005", 5.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 1.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "02-Apr-2003", 3.0));

        assertThat(calculator.calculate()).isEqualTo(1 + 3 + 5);
    }

    @Test
    public void testDuplicateEntries() {
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2005", 5.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2005", 5.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 1.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "02-Apr-2003", 3.0));

        assertThat(calculator.calculate()).isEqualTo(1 + 3 + 5);
    }

    @Test
    public void testOverflowOfMaxEntries() {
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 1.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "05-Mar-2002", 2.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "02-Apr-2003", 3.0));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2004", 4.0));

        assertThat(calculator.calculate()).isEqualTo(2 + 3 + 4);
    }

    @Test
    public void testScalingEffect() {
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "01-Jan-2001", 0.000001));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "05-Mar-2002", 0.000002));
        calculator.update(new CalculationService.LineData("INSTRUMENT1", "02-Apr-2003", 0.000003));

        double expectedMeanWithScaling = ((0.000001 * SCALE_FACTOR) + (0.000002 * SCALE_FACTOR) + (0.000003 * SCALE_FACTOR)) / SCALE_FACTOR;

        double calculatedSum = calculator.calculate();

        assertThat(calculatedSum).isEqualTo(expectedMeanWithScaling);
    }

}
