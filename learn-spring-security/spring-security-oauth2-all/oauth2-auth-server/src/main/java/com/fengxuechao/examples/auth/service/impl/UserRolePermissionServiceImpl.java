package com.fengxuechao.examples.auth.service.impl;

import com.fengxuechao.examples.auth.domain.Role;
import com.fengxuechao.examples.auth.service.UserRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/17
 */
@Service
public class UserRolePermissionServiceImpl implements UserRolePermissionService {
    @Override
    public List<Role> findRoleListByPermissionUrl(String requestUrl) {
        return null;
    }
}
