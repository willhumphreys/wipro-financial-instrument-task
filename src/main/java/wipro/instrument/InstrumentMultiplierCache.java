package wipro.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstrumentMultiplierCache {

    private static final long CACHE_DURATION_MS = 5000; // 5 seconds
    private final InstrumentPriceModifierService instrumentPriceModifierService;
    private final Map<String, Double> multiplierCache = new HashMap<>();
    private final Map<String, Long> lastFetchTime = new HashMap<>();

    public double getMultiplier(String instrument) {
        long currentTime = System.currentTimeMillis();
        if (!multiplierCache.containsKey(instrument) ||
                currentTime - lastFetchTime.getOrDefault(instrument, 0L) > CACHE_DURATION_MS) {
            Optional<InstrumentPriceModifier> modifierOptional =
                    instrumentPriceModifierService.findByName(instrument);
            if (modifierOptional.isPresent()) {
                double multiplier = modifierOptional.get()
                                                    .getMultiplier();
                multiplierCache.put(instrument, multiplier);
                lastFetchTime.put(instrument, currentTime);
                return multiplier;
            } else {
                multiplierCache.put(instrument, 1.000000); // Default multiplier
                lastFetchTime.put(instrument, currentTime);
            }
        }
        return multiplierCache.get(instrument);
    }
}
