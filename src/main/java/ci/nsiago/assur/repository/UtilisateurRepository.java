package ci.nsiago.assur.repository;

import ci.nsiago.assur.model.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Integer> {
    public Optional<UtilisateurEntity> findByEmail(String email);
}
