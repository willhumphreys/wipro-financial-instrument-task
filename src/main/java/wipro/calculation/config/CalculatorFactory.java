package wipro.calculation.config;

import org.springframework.stereotype.Component;
import wipro.calculation.domain.Calculator;
import wipro.calculation.domain.InstrumentCalculationRule;
import wipro.calculation.domain.MeanValuesCalculator;
import wipro.calculation.domain.StandardDeviationCalculator;

import java.util.List;

import static wipro.calculation.predicates.InstrumentPredicates.*;

@Component
public class CalculatorFactory {

    public List<Calculator> createCalculators() {
        var instrument1Condition = new InstrumentCalculationRule(
                "INSTRUMENT1_MEAN", isInstrument1,
                1_000_000L // Scale factor for INSTRUMENT1
        );
        var instrument2Nov2014Condition = new InstrumentCalculationRule(
                "INSTRUMENT2_MEAN_NOV2004", isInstrument2Nov2004,
                1_000_000_000L // Scale factor for INSTRUMENT2
        );

        var instrument3Condition = new InstrumentCalculationRule(
                "INSTRUMENT3_STD_DEV", isInstrument3,
                100_000_000L // Scale factor for INSTRUMENT1
        );

        return List.of(
                new MeanValuesCalculator(
                        instrument1Condition),
                new MeanValuesCalculator(instrument2Nov2014Condition),
                new StandardDeviationCalculator(instrument3Condition));
    }
}
