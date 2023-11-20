package wipro.calculation.domain;

import java.util.function.BiPredicate;

public record InstrumentCalculationRule(String identifier, BiPredicate<String, String> rule, long scaleFactor) {

    public boolean test(String instrument, String date) {
        return rule.test(instrument, date);
    }
}
