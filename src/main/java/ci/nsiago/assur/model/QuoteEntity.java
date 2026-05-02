package ci.nsiago.assur.model;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quote")
public class QuoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String quoteReference;

    private LocalDateTime createdAt;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private ProductType product;

    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;

    private Integer fiscalPower;
    private Double vehicleNewValue;
    private Double vehicleVenalValue;
    private Integer vehicleAge;

    private Double totalPremium;
}
