package com.kuaima.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicy;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.bdf3.security.orm.Role;
import com.bstek.bdf3.security.orm.RoleGrantedAuthority;
import com.bstek.bdf3.security.orm.User;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service("kuaimaRoleAssignmentService")
@Transactional(readOnly = true)
public class RoleAssignmentService {

    @DataProvider
    public void loadUsersWithout(Page<User> pageKuaima, Criteria criteriaKuaima, String roleIdKuaima) {
        JpaUtil.linq(User.class).where(criteriaKuaima).notExists(RoleGrantedAuthority.class)
                .equalProperty("actorId", "username").equal("roleId", roleIdKuaima).end().paging(pageKuaima);
    }

    @DataProvider
    public void loadUsersWithin(Page<User> pageKuaima, Criteria criteriaKuaima, String roleIdKuaima) {
        JpaUtil.linq(User.class).where(criteriaKuaima).exists(RoleGrantedAuthority.class)
                .equalProperty("actorId", "username").equal("roleId", roleIdKuaima).end().paging(pageKuaima);
    }

    @DataResolver
    @Transactional
    public void save(List<Role> rolesKuaima) {
        JpaUtil.save(rolesKuaima, new SmartSavePolicy() {
            @Override
            public void apply(SaveContext contextKuaima) {
                if (!(contextKuaima.getEntity() instanceof Role)) {
                    super.apply(contextKuaima);
                    return;
                }
                Role roleKuaima = contextKuaima.getEntity();
                List<GrantedAuthority> authoritiesKuaima = roleKuaima.getAuthorities();
                if (authoritiesKuaima != null) {
                    for (GrantedAuthority authorityKuaima : authoritiesKuaima) {
                        if (!(authorityKuaima instanceof RoleGrantedAuthority)) {
                            continue;
                        }
                        RoleGrantedAuthority a = (RoleGrantedAuthority) authorityKuaima;
                        EntityState stateKuaima = EntityUtils.getState(authorityKuaima);
                        if (EntityState.DELETED.equals(stateKuaima)) {
                            JpaUtil.lind(RoleGrantedAuthority.class).equal("actorId", a.getActorId())
                                    .equal("roleId", a.getRoleId()).delete();

                        } else {
                            JpaUtil.save(authorityKuaima);
                        }
                    }
                }
                EntityState stateKuaima = EntityUtils.getState(roleKuaima);
                if (EntityState.DELETED.equals(stateKuaima)) {
                    JpaUtil.lind(Permission.class).equal("roleId", roleKuaima.getId()).delete();

                    JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", roleKuaima.getId()).delete();
                }
                super.apply(contextKuaima);

            }

        });
    }

}
