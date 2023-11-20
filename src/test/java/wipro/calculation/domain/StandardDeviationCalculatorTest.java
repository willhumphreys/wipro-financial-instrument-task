package wipro.calculation.domain;

import org.junit.jupiter.api.Test;
import wipro.calculation.service.CalculationService.LineData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

public class StandardDeviationCalculatorTest {

    private final InstrumentCalculationRule rule = new InstrumentCalculationRule("INSTRUMENT_TEST", (instrument, date) -> true, 1_000_000L);


    @Test
    public void testStandardDeviationCalculation() {
        Calculator calculator = new StandardDeviationCalculator(rule);

        // Simulating updates with different values
        calculator.update(new LineData("INSTRUMENT_TEST", "01-Jan-2022", 2.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "02-Jan-2022", 4.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "03-Jan-2022", 6.0));

        // Calculating standard deviation
        double standardDeviation = calculator.calculate();

        // Expected standard deviation calculation
        double mean = (2.0 + 4.0 + 6.0) / 3;
        double variance = ((2.0 - mean) * (2.0 - mean) + (4.0 - mean) * (4.0 - mean) + (6.0 - mean) * (6.0 - mean)) / 3;
        double expectedStandardDeviation = Math.sqrt(variance);

        // Asserting the calculated standard deviation is as expected
        assertThat(standardDeviation).isCloseTo(expectedStandardDeviation, within(0.0001));
    }

    @Test
    public void testStandardDeviationWithNoData() {
        Calculator calculator = new StandardDeviationCalculator(rule);

        double standardDeviation = calculator.calculate();
        assertThat(standardDeviation).isEqualTo(0.0);
    }

    @Test
    public void testStandardDeviationWithSameDataPoints() {
        Calculator calculator = new StandardDeviationCalculator(rule);

        calculator.update(new LineData("INSTRUMENT_TEST", "01-Jan-2022", 2.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "02-Jan-2022", 2.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "03-Jan-2022", 2.0));

        double standardDeviation = calculator.calculate();
        assertThat(standardDeviation).isEqualTo(0.0);
    }

    @Test
    public void testStandardDeviationWithNegativeValues() {
        Calculator calculator = new StandardDeviationCalculator(rule);

        calculator.update(new LineData("INSTRUMENT_TEST", "01-Jan-2022", -2.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "02-Jan-2022", -4.0));
        calculator.update(new LineData("INSTRUMENT_TEST", "03-Jan-2022", -6.0));

        double standardDeviation = calculator.calculate();

        // Expected standard deviation calculation
        double mean = (-2.0 + -4.0 + -6.0) / 3;
        double variance = ((-2.0 - mean) * (-2.0 - mean) + (-4.0 - mean) * (-4.0 - mean) + (-6.0 - mean) * (-6.0 - mean)) / 3;
        double expectedStdDev = Math.sqrt(variance);

        assertThat(standardDeviation).isCloseTo(expectedStdDev, within(0.0001));
    }


    @Test
    void testWithSampleData() {

        Calculator calculator = new StandardDeviationCalculator(rule);

        calculator.update(new LineData("INSTRUMENT2", "24-May-1999", 9.518718202));
        calculator.update(new LineData("INSTRUMENT2", "25-May-1999", 9.519179643));
        calculator.update(new LineData("INSTRUMENT2", "26-May-1999", 9.542020046));

        var calculate = calculator.calculate();

        assertThat(calculate).isCloseTo(0.010877463300704627, within(0.0001));
    }
}
