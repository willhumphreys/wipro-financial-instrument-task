package wipro.calculation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wipro.calculation.service.CalculationService.LineData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MeanValuesCalculatorTest {

    @Mock
    private InstrumentCalculationRule rule1;

    private MeanValuesCalculator calculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculator = new MeanValuesCalculator(rule1);
    }

    @Test
    void testCalculateMean() {
        // Set up the mock behavior
        when(rule1.test("INSTRUMENT1", "01-Jan-1996")).thenReturn(true);
        when(rule1.test("INSTRUMENT1", "02-Jan-1996")).thenReturn(true);
        when(rule1.identifier()).thenReturn("INSTRUMENT1");
        when(rule1.scaleFactor()).thenReturn(1_000_000L);

        // Simulate updating the calculator
        calculator.update(new LineData("INSTRUMENT1", "01-Jan-1996", 2.4655));
        calculator.update(new LineData("INSTRUMENT1", "02-Jan-1996", 2.4685));

        // Perform calculation
        double result = calculator.calculate();

        // Check the results
        assertThat(result).isEqualTo(2.467);
    }
}
