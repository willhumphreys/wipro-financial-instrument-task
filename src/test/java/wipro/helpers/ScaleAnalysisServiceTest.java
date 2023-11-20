package wipro.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ScaleAnalysisServiceTest {

    private ScaleAnalysisService scaleAnalysisService;
    private File testFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        scaleAnalysisService = new ScaleAnalysisService();
        testFile = tempDir.resolve("testData.csv")
                          .toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("INSTRUMENT1,01-Jan-2022,10.123\n");
            writer.write("INSTRUMENT1,02-Jan-2022,10.1\n");
            writer.write("INSTRUMENT2,01-Jan-2022,5.12345\n");
            writer.write("INSTRUMENT2,02-Jan-2022,5.12\n");
        }
    }

    @Test
    void findMinMaxScaleValuesPerInstrument() {
        Map<String, ScaleAnalysisService.ScaleExtremes> result = scaleAnalysisService.findMinMaxScaleValuesPerInstrument(testFile);

        assertThat(result).isNotNull()
                          .hasSize(2)
                          .containsKeys("INSTRUMENT1", "INSTRUMENT2");

        // Check for INSTRUMENT1
        assertThat(result.get("INSTRUMENT1")
                         .getMinScale()).isEqualTo(1);
        assertThat(result.get("INSTRUMENT1")
                         .getMaxScale()).isEqualTo(3);

        // Check for INSTRUMENT2
        assertThat(result.get("INSTRUMENT2")
                         .getMinScale()).isEqualTo(2);
        assertThat(result.get("INSTRUMENT2")
                         .getMaxScale()).isEqualTo(5);
    }
}
