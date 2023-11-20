package wipro.calculation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wipro.calculation.config.CalculatorFactory;
import wipro.calculation.config.LatestValuesCalculatorFactory;
import wipro.calculation.domain.Calculator;
import wipro.helpers.ZellersCongruence;
import wipro.instrument.InstrumentMultiplierCache;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wipro.calculation.predicates.InstrumentPredicates.KNOWN_INSTRUMENTS;

/**
 * CalculationService is responsible for processing financial data streams.
 * It utilizes a variety of calculators to compute financial metrics based on the incoming data.
 * <p>
 * The service handles the stream of line data, applies multipliers, and aggregates results
 * from both standard and latest value calculators.
 */
@Service
@RequiredArgsConstructor
public class CalculationService {

    private final InstrumentMultiplierCache instrumentMultiplierCache;
    private final CalculatorFactory calculatorFactory;
    private final LatestValuesCalculatorFactory latestValuesCalculatorFactory;


    public Map<String, Double> processFinancialData(Stream<String> lines) {

        latestValuesCalculatorFactory.clear();

        List<Calculator> calculators = calculatorFactory.createCalculators();

        try (var stream = lines) {
            stream.filter(line -> line.split(",").length >= 3)
                  .map(this::processLine)
                  .filter(Objects::nonNull)
                  .forEach(data -> {

                      if (!KNOWN_INSTRUMENTS.contains(data.instrument())) {

                          var latestValueCalculator = latestValuesCalculatorFactory.getCalculator(data.instrument() + "_SUM_LATEST_10");

                          latestValueCalculator.update(data);
                      }

                      calculators.forEach(calculator -> calculator.update(data));

                  });
        }

        var allCalculators = Stream.concat(calculators.stream(), latestValuesCalculatorFactory.getCalculators()
                                                                                              .stream())
                                   .toList();
        return aggregateResults(allCalculators);
    }

    private LineData processLine(String line) {
        var parts = line.split(",");
        if (!ZellersCongruence.isBusinessDay(parts[1])) {
            return null;
        }
        double rawValue = Double.parseDouble(parts[2]);
        double rawMultiplier = instrumentMultiplierCache.getMultiplier(parts[0]);
        return new LineData(parts[0], parts[1], rawValue * rawMultiplier);
    }

    private Map<String, Double> aggregateResults(List<Calculator> calculators) {
        return calculators.stream()
                          .collect(Collectors.toMap(Calculator::getName, Calculator::calculate));
    }

    public record LineData(String instrument, String date, double value) {
    }
}
