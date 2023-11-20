package wipro.calculation.predicates;

import java.util.Set;
import java.util.function.BiPredicate;

public class InstrumentPredicates {
    public static final BiPredicate<String, String> isInstrument1 = (instrument, date) -> instrument.equals("INSTRUMENT1");
    public static final BiPredicate<String, String> isInstrument3 = (instrument, date) -> instrument.equals("INSTRUMENT3");
    public static final Set<String> KNOWN_INSTRUMENTS = Set.of("INSTRUMENT1", "INSTRUMENT2", "INSTRUMENT3");
    private static final String NOV_2014 = "-Nov-2014";
    public static final BiPredicate<String, String> isInstrument2Nov2004 = (instrument, date) -> instrument.equals("INSTRUMENT2") && date.contains(NOV_2014);
}
