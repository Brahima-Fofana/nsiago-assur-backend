package ci.nsiago.assur.repository;

import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.model.ValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<ValidationEntity, Integer> {
    public Optional<ValidationEntity> findByCode(String code);
    public Optional<ValidationEntity> findByUtilisateur(UtilisateurEntity user);
}
