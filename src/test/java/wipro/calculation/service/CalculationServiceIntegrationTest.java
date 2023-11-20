package wipro.calculation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public final class CalculationServiceIntegrationTest {

    private static final Logger logger = LogManager.getLogger(CalculationService.class);

    @Autowired
    private CalculationService calculationService;

    @Test
    void testWithExampleInput() throws IOException {
        long startTime = System.currentTimeMillis();

        final File financialDataFile = new File("example_input.txt");
        try (var lines = Files.lines(Paths.get(financialDataFile.getAbsolutePath()), UTF_8)) {
            Map<String, Double> stringDoubleMap = calculationService.processFinancialData(lines);

            assertThat(stringDoubleMap.size()).isEqualTo(3);
            assertThat(stringDoubleMap.get("INSTRUMENT1_MEAN")).isEqualTo(3.3675917205584787);
            assertThat(stringDoubleMap.get("INSTRUMENT2_MEAN_NOV2004")).isEqualTo(9.25755836695);
            assertThat(stringDoubleMap.get("INSTRUMENT3_STD_DEV")).isEqualTo(15.218741355744713);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("processFinancialData execution time: " + duration + " milliseconds");
    }


    @Test
    void testSumOfLast10ValuesForInstrument4() throws IOException {

        long startTime = System.currentTimeMillis();

        final File financialDataFile = new File("instrument4_input.txt");
        try (var lines = Files.lines(Paths.get(financialDataFile.getAbsolutePath()), UTF_8)) {
            Map<String, Double> stringDoubleMap = calculationService.processFinancialData(lines);


            assertThat(stringDoubleMap.containsKey("INSTRUMENT4_SUM_LATEST_10")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT4_SUM_LATEST_10")).isEqualTo(24.1);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT5_SUM_LATEST_10")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT5_SUM_LATEST_10")).isEqualTo(41.1);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("processFinancialData execution time: " + duration + " milliseconds");
    }

}
