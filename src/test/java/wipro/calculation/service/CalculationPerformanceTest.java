package wipro.calculation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static wipro.calculation.service.TestDataGenerator.generateDataLine;

@SpringBootTest
public class CalculationPerformanceTest {

    private static final Logger logger = LogManager.getLogger(CalculationPerformanceTest.class);

    private static final long TOTAL_ROWS = 10_000_000;

    @Autowired
    private CalculationService calculationService;

    @Test
    public void testProcessFinancialDataWith10MillionRows() throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        PrintWriter writer = new PrintWriter(pos, true);

        // Task to generate data
        executorService.submit(() -> {
            long rowCount = 0;
            while (rowCount < TOTAL_ROWS) {
                var dataLine = generateDataLine();
                writer.println(dataLine);

                rowCount++;
            }
            writer.close();
        });

        long startTime = System.currentTimeMillis();

        // Process the data stream
        try (Stream<String> stream = new BufferedReader(new InputStreamReader(pis)).lines()) {
            var stringDoubleMap = calculationService.processFinancialData(stream);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT4_SUM_LATEST_10")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT4_SUM_LATEST_10")).isEqualTo(24.3737);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT1_MEAN")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT1_MEAN")).isEqualTo(2.49980485644901);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT2_MEAN_NOV2004")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT2_MEAN_NOV2004")).isEqualTo(2.496728702151776);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT3_STD_DEV")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT3_STD_DEV")).isEqualTo(0.28864378933842316);

            assertThat(stringDoubleMap.containsKey("INSTRUMENT5_SUM_LATEST_10")).isTrue();
            assertThat(stringDoubleMap.get("INSTRUMENT5_SUM_LATEST_10")).isEqualTo(25.1951);

        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("processFinancialData execution time: %d milliseconds for %,d rows".formatted(duration, TOTAL_ROWS));

        executorService.shutdown();
        boolean finishedInTime = executorService.awaitTermination(10, TimeUnit.MINUTES); // Allowing ample time for processing

        if (!finishedInTime) {
            throw new IllegalStateException("Test did not finish processing within the expected time.");
        }
    }
}
