package wipro.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstrumentPriceModifierService {

    private final InstrumentPriceModifierRepository instrumentPriceModifierRepository;


    public Optional<InstrumentPriceModifier> findByName(String name) {
        return instrumentPriceModifierRepository.findByName(name);
    }

}
