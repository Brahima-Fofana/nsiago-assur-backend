package ci.nsiago.assur.dto;

import ci.nsiago.assur.model.ProductType;
import ci.nsiago.assur.model.VehicleCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuoteResponseDto {
    private String quoteReference;
    private LocalDateTime endDate;
    private Double price;
    private ProductType product;
    private VehicleCategory vehicleCategory;
    private Integer fiscalPower;
    private Double vehicleNewValue;
    private Double vehicleVenalValue;
    private Integer vehicleAge;
}
