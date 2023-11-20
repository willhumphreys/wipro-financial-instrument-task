package wipro.instrument;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentPriceModifierRepository extends JpaRepository<InstrumentPriceModifier, Long> {

    Optional<InstrumentPriceModifier> findByName(String name);
}
