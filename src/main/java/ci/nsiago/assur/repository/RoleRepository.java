package ci.nsiago.assur.repository;

import ci.nsiago.assur.model.RoleEntity;
import ci.nsiago.assur.model.TypeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    public Optional<RoleEntity> findByLibelle(TypeRole libelle);
}
