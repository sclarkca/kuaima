package com.bstek.bdf3.security.service;

import org.malagu.linq.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.security.orm.User;

/**
 * Spring Security的
 * {@link org.springframework.security.core.userdetails.UserDetailsService}
 * 接口的默认实现
 * 
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年2月27日
 */
@Service
@Transactional(readOnly = true)
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private GrantedAuthorityService grantedAuthorityService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			User user = JpaUtil.getOne(User.class, username);
			user.setAuthorities(grantedAuthorityService.getGrantedAuthorities(user));//NOSONAR
			return user;
		} catch (Exception e) {
			logger.info("Exception", e);
			throw new UsernameNotFoundException("Not Found");
		}
	}
}
