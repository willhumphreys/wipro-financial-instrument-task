package wipro.helpers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZellersCongruenceTest {

    @Test
    void testBusinessDay() {
        assertThat(ZellersCongruence.isBusinessDay("17-Dec-2014")).isTrue(); // Wednesday
        assertThat(ZellersCongruence.isBusinessDay("18-Dec-2014")).isTrue(); // Thursday
    }

    @Test
    void testNonBusinessDay() {
        assertThat(ZellersCongruence.isBusinessDay("20-Dec-2014")).isFalse(); // Saturday
        assertThat(ZellersCongruence.isBusinessDay("21-Dec-2014")).isFalse(); // Sunday
    }

    @Test
    void testEdgeCases() {
        assertThat(ZellersCongruence.isBusinessDay("31-Dec-2014")).isTrue(); // Wednesday
        assertThat(ZellersCongruence.isBusinessDay("01-Jan-2015")).isTrue(); // Thursday
    }

    @Test
    void testInvalidDate() {
        assertThatThrownBy(() -> ZellersCongruence.isBusinessDay("31-ABC-2014"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid month");
    }
}
