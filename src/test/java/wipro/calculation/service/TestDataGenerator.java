package wipro.calculation.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Random;

public class TestDataGenerator {

    private static final String[] INSTRUMENTS = {"INSTRUMENT1", "INSTRUMENT2", "INSTRUMENT3", "INSTRUMENT4", "INSTRUMENT5"};
    private static final Random random = new Random(0); // Fixed seed for repeatability
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US); // Locale so we generate Sep and not Sept
    private static int instrumentIndex = 0;

    static String generateDataLine() {
        String instrument = INSTRUMENTS[instrumentIndex];
        instrumentIndex = (instrumentIndex + 1) % INSTRUMENTS.length;

        String date = generateDate();
        double value = generateValue();

        return String.format("%s,%s,%.4f", instrument, date, value);
    }

    private static String generateDate() {
        LocalDate date;


        LocalDate startInclusive = LocalDate.of(2000, 1, 1);
        LocalDate endExclusive = LocalDate.of(2014, 12, 20);
        long days = ChronoUnit.DAYS.between(startInclusive, endExclusive);
        date = startInclusive.plusDays(random.nextInt((int) days));


        return date.format(dateFormatter);
    }

    private static double generateValue() {
        // Generate a fixed pattern of values
        return 2.0 + random.nextDouble(); // Values between 2.0 and 3.0
    }

}
