package wipro.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ZellersCongruence {

    private static final Logger logger = LogManager.getLogger(ZellersCongruence.class);


    public static boolean isBusinessDay(String dateString) {
        int[] dateParts = parseDate(dateString);
        int day = dateParts[0];
        int month = dateParts[1];
        int year = dateParts[2];

        if (month < 3) {
            month += 12;
            year -= 1;
        }

        int k = year % 100;
        int j = year / 100;

        int dayOfWeek = (day + (13 * (month + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;

        // Zeller's Congruence returns 0 for Saturday and 1 for Sunday
        return !(dayOfWeek == 0 || dayOfWeek == 1);
    }

    private static int[] parseDate(String dateString) {
        String[] parts = dateString.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = monthToNumber(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return new int[]{day, month, year};
    }

    private static int monthToNumber(String month) {
        return switch (month) {
            case "Jan" -> 1;
            case "Feb" -> 2;
            case "Mar" -> 3;
            case "Apr" -> 4;
            case "May" -> 5;
            case "Jun" -> 6;
            case "Jul" -> 7;
            case "Aug" -> 8;
            case "Sep" -> 9;
            case "Oct" -> 10;
            case "Nov" -> 11;
            case "Dec" -> 12;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

    public static void main(String[] args) {
        String filePath = "example_input.txt";
        int weekendCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String dateString = parts[1];
                    if (!isBusinessDay(dateString)) {
                        weekendCount++;
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }

        System.out.println("Number of weekends: " + weekendCount);
    }

}
