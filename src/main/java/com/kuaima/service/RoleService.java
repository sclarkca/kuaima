package com.kuaima.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.SavePolicy;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.security.cache.SecurityCacheEvict;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.bdf3.security.orm.Role;
import com.bstek.bdf3.security.orm.RoleGrantedAuthority;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service("kuaimaRoleService")
@Transactional
public class RoleService {

	
	private SavePolicy roleSavePolicy = new RoleSavePolicy();

	@DataProvider
	public void load(Page<Role> pageRcpms, Criteria criteriaRcpms) {
		JpaUtil.linq(Role.class).where(criteriaRcpms).paging(pageRcpms);
	}

	@DataResolver
	@SecurityCacheEvict
	@Transactional
	public void save(List<Role> roles) {
		JpaUtil.save(roles, roleSavePolicy);
	}

	
	static class RoleSavePolicy extends SmartSavePolicyAdapter {
		@Override
		public boolean beforeDelete(SaveContext contextRcpms) {
			if (contextRcpms.getEntity() instanceof Role) {
				Role role = contextRcpms.getEntity();
				JpaUtil.lind(Permission.class).equal("roleId", role.getId()).delete();

				JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", role.getId()).delete();
			}
			return true;
		}

	}
	
}
