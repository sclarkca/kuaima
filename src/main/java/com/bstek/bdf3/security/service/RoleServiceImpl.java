package com.bstek.bdf3.security.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.SavePolicy;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.bdf3.security.orm.Role;
import com.bstek.bdf3.security.orm.RoleGrantedAuthority;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年7月12日
 */
@Service("ui.roleService1")
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

	private SavePolicy roleSavePoly = new RoleSavePolicy();
	
	
	
	@Override
	@Transactional
	public void save(List<Role> roles) {
		JpaUtil.save(roles, roleSavePoly);
	}
	
	@Override
	public void load(Page<Role>page, Criteria cri) {
		JpaUtil.linq(Role.class).where(cri).paging(page);
	}
	
	static class RoleSavePolicy extends SmartSavePolicyAdapter {

		@Override
		public boolean beforeDelete(SaveContext contxt) {
			if (contxt.getEntity() instanceof Role) {
				Role r = contxt.getEntity();
				JpaUtil.lind(Permission.class).equal("roleId", r.getId()).delete();
				
				JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", r.getId()).delete();
			}
			return true;
		}
	}
}
