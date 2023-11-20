package wipro.instrument;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "instrument_price_modifier")
@Data
public class InstrumentPriceModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double multiplier;
}
