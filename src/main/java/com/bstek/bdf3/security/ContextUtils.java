package com.bstek.bdf3.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bstek.bdf3.security.orm.User;
import com.bstek.dorado.view.resolver.ClientRunnableException;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年8月10日
 */
public abstract class ContextUtils {
    private ContextUtils() {
    }

    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();

        }
        return null;
    }

    public static String getLoginUsername() {
        User user = getLoginUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public static String getOaUsername() {
        StringBuilder script = new StringBuilder();
        User user = getLoginUser();
        if (user != null) {
            String username = user.getUsername();
            if (StringUtils.isNotBlank(username)) {
                return username.substring("RCAMS".length(), username.length());
            } else {
                script.append("dorado.widget.NotifyTipManager.notify('用户已掉线,请重新登录!');");
                throw new ClientRunnableException(script.toString());
            }
        }
        return null;
    }
}
