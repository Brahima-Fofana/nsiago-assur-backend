package ci.nsiago.assur.controllers;

import ci.nsiago.assur.dto.QuoteRequestDto;
import ci.nsiago.assur.dto.QuoteResponseDto;
import ci.nsiago.assur.service.QuoteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/simulations")
@RestController
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<QuoteResponseDto> simulate(@Valid @RequestBody QuoteRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quoteService.simulate(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponseDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(quoteService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuoteResponseDto>> getAll() {
        return ResponseEntity.ok(quoteService.getAll());
    }
}
