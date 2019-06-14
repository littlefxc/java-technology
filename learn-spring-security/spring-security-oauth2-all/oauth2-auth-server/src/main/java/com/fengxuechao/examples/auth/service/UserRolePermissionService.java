package com.fengxuechao.examples.auth.service;

import com.fengxuechao.examples.auth.domain.Role;

import java.util.List;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
public interface UserRolePermissionService {

    List<Role> findRoleListByPermissionUrl(String requestUrl);
}
