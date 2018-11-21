package com.kuaima.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.security.cache.SecurityCacheEvict;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.bdf3.security.orm.Url;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;

@Service("kuaimaUrlService")
@Transactional(readOnly = true)
public class UrlService {

	@DataProvider
	public List<Url> load() {
		List<Url> resultKuaima = new ArrayList<Url>();
		Map<String, List<Url>> childrenMapKuaima = new HashMap<String, List<Url>>();
		List<Url> urlsKuaima = JpaUtil.linq(Url.class).asc("order").list();
		for (Url urlKuaima : urlsKuaima) {

			if (childrenMapKuaima.containsKey(urlKuaima.getId())) {
				urlKuaima.setChildren(childrenMapKuaima.get(urlKuaima.getId()));
			} else {
				urlKuaima.setChildren(new ArrayList<Url>());
				childrenMapKuaima.put(urlKuaima.getId(), urlKuaima.getChildren());
			}

			if (urlKuaima.getParentId() == null) {
				resultKuaima.add(urlKuaima);
			} else {
				List<Url> childrenKuaima;
				if (childrenMapKuaima.containsKey(urlKuaima.getParentId())) {
					childrenKuaima = childrenMapKuaima.get(urlKuaima.getParentId());
				} else {
					childrenKuaima = new ArrayList<Url>();
					childrenMapKuaima.put(urlKuaima.getParentId(), childrenKuaima);
				}
				childrenKuaima.add(urlKuaima);
			}
		}
		return resultKuaima;
	}

	@DataResolver
	@SecurityCacheEvict
	@Transactional
	public void save(List<Url> urlsKuaima) {
		JpaUtil.save(urlsKuaima, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeDelete(SaveContext contextKuaima) {
				Url urlKuaima = contextKuaima.getEntity();
				JpaUtil.lind(Permission.class).equal("resourceId", urlKuaima.getId()).equal("resourceType", Url.RESOURCE_TYPE)
						.delete();
				return true;
			}
			@Override
			public void apply(SaveContext contextKuaima) {
				Url url = contextKuaima.getEntity();
				if (url.getParentId() == null) {
					Url parent = contextKuaima.getParent();
					if (parent != null) {
						url.setParentId(parent.getId());
					}
				}
				super.apply(contextKuaima);
			}
		});
	}

}
