package ci.nsiago.assur.repository;

import ci.nsiago.assur.model.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Integer> {
    public Optional<QuoteEntity> findByQuoteReference(String quoteReference);
}
