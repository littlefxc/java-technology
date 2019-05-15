package com.fengxuechao.examples.auth.userdetails;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/15
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
