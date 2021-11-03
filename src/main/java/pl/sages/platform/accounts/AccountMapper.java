package pl.sages.platform.accounts;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AccountMapper {


    private static final String ROLE_PREFIX = "ROLE_";

    public Set<String> toRoleTransferObjects(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(roleName -> roleName.replace(ROLE_PREFIX, ""))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public Set<Role> toRolesObjects(Set<String> roleTransferObjects) {
        return roleTransferObjects.stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    public abstract AccountTransferObject toAccountTransferObject(Account account);

    public abstract Account toAccount(AccountTransferObject accountTransferObject);

}
