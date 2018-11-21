package com.bstek.bdf3.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.malagu.linq.JpaUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.security.Constants;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.bdf3.security.orm.Url;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月30日
 */
@Service
@Transactional(readOnly = true)
public class UrlServiceCacheImpl implements UrlServiceCache {


	@Override
	@Cacheable(cacheNames = Constants.URL_TREE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public List<Url> findTree() {
		List<Url> result = new ArrayList<Url>();
		List<Url> urls = JpaUtil.linq(Url.class).asc("order").list();
		List<Permission> permissions = JpaUtil.linq(Permission.class).equal("resourceType", Url.RESOURCE_TYPE).list();
		Map<String, Url> urlMap = new HashMap<String, Url>(urls.size());
		Map<String, List<Url>> childrenMapTmp = new HashMap<String, List<Url>>(urls.size());

		for (Url url : urls) {
			urlMap.put(url.getId(), url);
			if (childrenMapTmp.containsKey(url.getId())) {
				url.setChildren(childrenMapTmp.get(url.getId()));
			} else {
				url.setChildren(new ArrayList<Url>());
				childrenMapTmp.put(url.getId(), url.getChildren());
			}

			if (url.getParentId() == null) {
				result.add(url);
			} else {
				List<Url> children;
				if (childrenMapTmp.containsKey(url.getParentId())) {
					children = childrenMapTmp.get(url.getParentId());
				} else {
					children = new ArrayList<Url>();
					childrenMapTmp.put(url.getParentId(), children);
				}
				children.add(url);
			}
		}
		for (Permission permission : permissions) {
			Url url = urlMap.get(permission.getResourceId());
			List<ConfigAttribute> configAttributes = url.getAttributes();
			if (configAttributes == null) {
				configAttributes = new ArrayList<ConfigAttribute>();
				url.setAttributes(configAttributes);
			}
			configAttributes.add(permission);
		}

		return result;
	}

}
