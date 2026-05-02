package ci.nsiago.assur.dto;

import ci.nsiago.assur.model.ProductType;
import ci.nsiago.assur.model.VehicleCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuoteRequestDto {
    @NotNull(message = "Le produit est obligatoire")
    private ProductType product;

    @NotNull(message = "La catégorie du véhicule est obligatoire")
    private VehicleCategory vehicleCategory;

    @NotNull
    @Min(value = 1, message = "La puissance fiscale doit être positive")
    private Integer fiscalPower;

    @NotNull
    @Min(value = 0, message = "La valeur à neuf doit être positive")
    private Double vehicleNewValue;

    @NotNull
    @Min(value = 0, message = "La valeur vénale doit être positive")
    private Double vehicleVenalValue;

    @NotNull
    @Min(value = 0, message = "L'âge du véhicule doit être positif")
    private Integer vehicleAge;
}
