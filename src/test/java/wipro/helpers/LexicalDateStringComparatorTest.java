package wipro.helpers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LexicalDateStringComparatorTest {

    private final LexicalDateStringComparator comparator = new LexicalDateStringComparator();

    @Test
    void testCompareWithDifferentYears() {
        assertThat(comparator.compare("01-Jan-1999", "01-Jan-2000")).isNegative();
        assertThat(comparator.compare("01-Jan-2000", "01-Jan-1999")).isPositive();
    }

    @Test
    void testCompareWithDifferentMonths() {
        assertThat(comparator.compare("01-Jan-1999", "01-Feb-1999")).isNegative();
        assertThat(comparator.compare("01-Mar-1999", "01-Feb-1999")).isPositive();
    }

    @Test
    void testCompareWithDifferentDays() {
        assertThat(comparator.compare("01-Jan-1999", "02-Jan-1999")).isNegative();
        assertThat(comparator.compare("03-Jan-1999", "02-Jan-1999")).isPositive();
    }

    @Test
    void testCompareWithSameDate() {
        assertThat(comparator.compare("01-Jan-1999", "01-Jan-1999")).isZero();
    }

    @Test
    void testCompareWithInvalidFormat() {
        assertThatThrownBy(() -> comparator.compare("1999-01-01", "01-Jan-1999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid date format");
    }

    @Test
    void testCompareWithInvalidMonth() {
        assertThatThrownBy(() -> comparator.compare("01-Abc-1999", "01-Jan-1999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid month");
    }
}
