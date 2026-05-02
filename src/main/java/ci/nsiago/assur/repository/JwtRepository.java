package ci.nsiago.assur.repository;

import ci.nsiago.assur.model.JwtEntity;
import ci.nsiago.assur.model.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtEntity, Integer> {
    public Optional<JwtEntity> findByValueAndExpirerAndDesactiver(String value, boolean expirer, boolean desactiver);
    @Query("FROM JwtEntity j WHERE j.utilisateur.email = :email AND j.expirer = :expirer AND j.desactiver = :desactiver")
    public Optional<JwtEntity> findUtilisateurTokenValid(String email, boolean expirer, boolean desactiver);
    public List<JwtEntity> findAllByUtilisateur(UtilisateurEntity user);
    public void deleteAllByExpirerAndDesactiver(boolean expirer, boolean desactiver);
}
