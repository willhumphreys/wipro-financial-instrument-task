package wipro.calculation.domain;

import wipro.calculation.service.CalculationService;

public interface Calculator {
    void update(CalculationService.LineData lineData);

    double calculate();

    String getName();
}
