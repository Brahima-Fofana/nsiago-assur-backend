package ci.nsiago.assur.service;

import ci.nsiago.assur.model.RoleEntity;
import ci.nsiago.assur.model.TypeRole;
import ci.nsiago.assur.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<RoleEntity> findLibelle(TypeRole libelle){
        return roleRepository.findByLibelle(libelle);
    }
    public void save(RoleEntity role){
        roleRepository.save(role);
    }
}
