package wipro.calculation.config;

import org.springframework.stereotype.Component;
import wipro.calculation.domain.Calculator;
import wipro.calculation.domain.InstrumentCalculationRule;
import wipro.calculation.domain.LatestValuesCalculator;
import wipro.helpers.LexicalDateStringComparator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LatestValuesCalculatorFactory {

    public static final LexicalDateStringComparator DATE_COMPARATOR = new LexicalDateStringComparator();
    public static final int MAX_ENTRIES = 10;

    private final Map<String, Calculator> calculators = new HashMap<>();

    public Calculator getCalculator(String identifier) {

        var instrumentCalculationRule = new InstrumentCalculationRule(identifier, null, 1_000_000_000L);
        var calculator = new LatestValuesCalculator(instrumentCalculationRule, DATE_COMPARATOR, MAX_ENTRIES);

        calculators.putIfAbsent(identifier, calculator);

        return calculators.get(identifier);
    }


    public Collection<Calculator> getCalculators() {
        return calculators.values();
    }

    public void clear() {
        calculators.clear();
    }
}
