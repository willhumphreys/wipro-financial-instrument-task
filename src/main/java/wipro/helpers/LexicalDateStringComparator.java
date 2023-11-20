package wipro.helpers;

import java.util.Comparator;

public class LexicalDateStringComparator implements Comparator<String> {

    @Override
    public int compare(String dateStr1, String dateStr2) {
        // Reorder date strings into a lexically comparable format (yyyy-MM-dd)
        String reorderedDateStr1 = reorderDateString(dateStr1);
        String reorderedDateStr2 = reorderDateString(dateStr2);

        return reorderedDateStr1.compareTo(reorderedDateStr2);
    }

    private String reorderDateString(String dateStr) {
        if (dateStr == null || dateStr.length() != 11) {
            throw new IllegalArgumentException("Invalid date format. Expected dd-MMM-yyyy.");
        }

        String day = dateStr.substring(0, 2);
        String month = dateStr.substring(3, 6);
        String year = dateStr.substring(7, 11);

        // Convert month to a two-digit number
        String monthNumber = convertMonthToNumber(month);

        return year + "-" + monthNumber + "-" + day;
    }

    private String convertMonthToNumber(String month) {
        return switch (month) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }
}
