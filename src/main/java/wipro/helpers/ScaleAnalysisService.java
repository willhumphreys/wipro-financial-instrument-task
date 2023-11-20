package wipro.helpers;

import lombok.Data;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * ScaleAnalysisService is a utility for analyzing the precision of financial instruments
 * by finding the minimum and maximum scale of BigDecimal values per instrument in a financial data file.
 * It provides a main method to execute the service, making it easy to use as a standalone application.
 * This class serves as an entry point for processing files to determine the range of decimal precision
 * across different instruments, which can be crucial for financial data analysis and validation.
 * <p>
 * The main method reads a specified file and prints out the scale extremes for each instrument to the log.
 * <p>
 * Usage example from the command line:
 * <pre>{@code
 * java -cp your-classpath-here wipro.helpers.ScaleAnalysisService path/to/financial_data.csv
 * }</pre>
 * <p>
 * Or, to use within code:
 * <pre>{@code
 * File dataFile = new File("path/to/financial_data.csv");
 * ScaleAnalysisService scaleAnalysisService = new ScaleAnalysisService();
 * Map<String, ScaleAnalysisService.ScaleExtremes> scaleExtremesPerInstrument =
 *     scaleAnalysisService.findMinMaxScaleValuesPerInstrument(dataFile);
 * scaleExtremesPerInstrument.forEach((instrument, extremes) ->
 *     System.out.println(instrument + ": " + extremes));
 * }</pre>
 */

public final class ScaleAnalysisService {
    private static final Logger logger = LogManager.getLogger(ScaleAnalysisService.class);

    public static void main(String[] args) {
        var scaleInfo = new ScaleAnalysisService().findMinMaxScaleValuesPerInstrument(new File("example_input.txt"));

        logger.info(scaleInfo);
    }

    public Map<String, ScaleExtremes> findMinMaxScaleValuesPerInstrument(File financialDataFile) {
        Map<String, ScaleExtremes> instrumentExtremesMap = new HashMap<>();

        try (var stream = Files.lines(Paths.get(financialDataFile.getAbsolutePath()), UTF_8)) {
            stream.map(line -> line.split(","))
                  .filter(parts -> parts.length >= 3)
                  .forEach(parts -> {
                      String instrument = parts[0];
                      BigDecimal value = new BigDecimal(parts[2]);
                      int scale = value.scale();

                      instrumentExtremesMap.computeIfAbsent(instrument, k -> new ScaleExtremes())
                                           .updateExtremes(scale, value);
                  });
        } catch (IOException e) {
            logger.error("Error processing file: " + e.getMessage());
        }

        return instrumentExtremesMap;
    }

    @ToString
    @Data
    public static class ScaleExtremes {
        private int minScale = Integer.MAX_VALUE;
        private int maxScale = 0;
        private BigDecimal minScaleValue = null;
        private BigDecimal maxScaleValue = null;

        public void updateExtremes(int scale, BigDecimal value) {
            if (scale > this.maxScale) {
                this.maxScale = scale;
                this.maxScaleValue = value;
            }
            if (scale < this.minScale) {
                this.minScale = scale;
                this.minScaleValue = value;
            }
        }
    }
}
