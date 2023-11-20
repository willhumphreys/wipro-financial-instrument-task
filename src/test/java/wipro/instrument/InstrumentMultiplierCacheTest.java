package wipro.instrument;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstrumentMultiplierCacheTest {

    private InstrumentPriceModifierService instrumentPriceModifierService;
    private InstrumentMultiplierCache instrumentMultiplierCache;

    @BeforeEach
    void setUp() {
        instrumentPriceModifierService = mock(InstrumentPriceModifierService.class);
        instrumentMultiplierCache = new InstrumentMultiplierCache(instrumentPriceModifierService);
    }

    @Test
    void getMultiplier_WhenModifierExists() {
        double expectedMultiplier = 2.0;
        InstrumentPriceModifier modifier = new InstrumentPriceModifier();
        modifier.setMultiplier(expectedMultiplier);

        when(instrumentPriceModifierService.findByName("INSTRUMENT1"))
                .thenReturn(Optional.of(modifier));

        double multiplier = instrumentMultiplierCache.getMultiplier("INSTRUMENT1");

        assertThat(multiplier).isEqualTo(expectedMultiplier);
    }

    @Test
    void getMultiplier_WhenModifierDoesNotExist() {

        when(instrumentPriceModifierService.findByName("INSTRUMENT2"))
                .thenReturn(Optional.empty());

        double multiplier = instrumentMultiplierCache.getMultiplier("INSTRUMENT2");

        assertThat(multiplier).isEqualTo(1.0); // Default multiplier
    }
}
