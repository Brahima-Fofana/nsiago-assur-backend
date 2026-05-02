package ci.nsiago.assur.service;

import ci.nsiago.assur.dto.QuoteRequestDto;
import ci.nsiago.assur.dto.QuoteResponseDto;
import ci.nsiago.assur.model.QuoteEntity;
import ci.nsiago.assur.model.VehicleCategory;
import ci.nsiago.assur.repository.QuoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteResponseDto simulate(QuoteRequestDto request) {
        // 1. Valider la catégorie par rapport au produit
        validateCategoryForProduct(request);

        // 2. Calculer la prime
        double totalPremium = calculateTotalPremium(request);

        // 3. Construire et sauvegarder le devis
        QuoteEntity quote = new QuoteEntity();
        quote.setQuoteReference(generateReference());
        quote.setCreatedAt(LocalDateTime.now());
        quote.setEndDate(LocalDateTime.now().plusWeeks(2));
        quote.setProduct(request.getProduct());
        quote.setVehicleCategory(request.getVehicleCategory());
        quote.setFiscalPower(request.getFiscalPower());
        quote.setVehicleNewValue(request.getVehicleNewValue());
        quote.setVehicleVenalValue(request.getVehicleVenalValue());
        quote.setVehicleAge(request.getVehicleAge());
        quote.setTotalPremium(totalPremium);

        quoteRepository.save(quote);

        // 4. Retourner le DTO
        return toResponseDto(quote);
    }

    private double calculateTotalPremium(QuoteRequestDto req) {
        return switch (req.getProduct()) {
            case PAPILLON      -> rc(req) + dommages(req) + vol(req);
            case DOUBY         -> rc(req) + dommages(req) + tierceCollision(req);
            case DOUYOU        -> rc(req) + dommages(req) + tierceCollision(req) + incendie(req);
            case TOUTOURISQUOU -> rc(req) + dommages(req) + tierceCollision(req)
                    + tiercePlafonnee(req) + vol(req) + incendie(req);
        };
    }

    private double rc(QuoteRequestDto req) {
        int cv = req.getFiscalPower();
        if (cv <= 2)       return 37_601;
        else if (cv <= 6)  return 45_181;
        else if (cv <= 10) return 51_078;
        else if (cv <= 14) return 65_677;
        else if (cv <= 23) return 86_456;
        else               return 104_143;
    }

    private double dommages(QuoteRequestDto req) {
        if (req.getVehicleAge() > 5) return 0;
        return req.getVehicleNewValue() * 0.026;
    }

    private double tierceCollision(QuoteRequestDto req) {
        if (req.getVehicleAge() > 8) return 0;
        return req.getVehicleNewValue() * 0.0165;
    }

    private double tiercePlafonnee(QuoteRequestDto req) {
        if (req.getVehicleAge() > 10) return 0;
        double prime = (req.getVehicleVenalValue() * 0.50) * 0.042;
        return Math.max(prime, 100_000);
    }

    private double vol(QuoteRequestDto req) {
        return req.getVehicleVenalValue() * 0.0014;
    }

    private double incendie(QuoteRequestDto req) {
        return req.getVehicleVenalValue() * 0.0015;
    }

    private void validateCategoryForProduct(QuoteRequestDto req) {
        boolean valid = switch (req.getProduct()) {
            case PAPILLON      -> req.getVehicleCategory() == VehicleCategory.CAT_201;
            case DOUBY         -> req.getVehicleCategory() == VehicleCategory.CAT_202;
            case DOUYOU        -> req.getVehicleCategory() == VehicleCategory.CAT_201
                    || req.getVehicleCategory() == VehicleCategory.CAT_202;
            case TOUTOURISQUOU -> req.getVehicleCategory() == VehicleCategory.CAT_201;
        };

        if (!valid) {
            throw new IllegalArgumentException(
                    "Catégorie " + req.getVehicleCategory() + " non éligible au produit " + req.getProduct()
            );
        }
    }

    private String generateReference() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("QT");
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public QuoteResponseDto toResponseDto(QuoteEntity quote) {
        QuoteResponseDto dto = new QuoteResponseDto();
        dto.setQuoteReference(quote.getQuoteReference());
        dto.setEndDate(quote.getEndDate());
        dto.setPrice(quote.getTotalPremium());
        dto.setProduct(quote.getProduct());
        dto.setVehicleCategory(quote.getVehicleCategory());
        dto.setFiscalPower(quote.getFiscalPower());
        dto.setVehicleNewValue(quote.getVehicleNewValue());
        dto.setVehicleVenalValue(quote.getVehicleVenalValue());
        dto.setVehicleAge(quote.getVehicleAge());

        return dto;
    }

    public QuoteResponseDto getById(int id) {
        QuoteEntity quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis introuvable avec l'id : " + id));
        return toResponseDto(quote);
    }

    public List<QuoteResponseDto> getAll() {
        return quoteRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
