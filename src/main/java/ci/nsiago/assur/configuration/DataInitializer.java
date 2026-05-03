package ci.nsiago.assur.configuration;

import ci.nsiago.assur.model.RoleEntity;
import ci.nsiago.assur.model.TypeRole;
import ci.nsiago.assur.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(TypeRole type : TypeRole.values()){
            if(roleService.findLibelle(type).isEmpty()){
                RoleEntity role = new RoleEntity();
                role.setLibelle(type);
                roleService.save(role);
            }
        }
    }
}
