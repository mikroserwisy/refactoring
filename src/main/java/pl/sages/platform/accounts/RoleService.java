package pl.sages.platform.accounts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sages.platform.common.configuration.TransactionalService;

@TransactionalService
@RequiredArgsConstructor
public class RoleService {

    @NonNull
    private final RoleRepository roleRepository;

    public Role getRole(String name) {
        return roleRepository.getByName(name)
                .orElseGet(() -> createRole(name));
    }

    private Role createRole(String name) {
        Role role = new Role(name);
        return roleRepository.saveAndFlush(role);
    }

}
