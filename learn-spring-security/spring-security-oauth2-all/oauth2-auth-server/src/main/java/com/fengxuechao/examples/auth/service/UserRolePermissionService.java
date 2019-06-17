package com.fengxuechao.examples.auth.service;

import com.fengxuechao.examples.auth.domain.Role;

import java.util.List;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
public interface UserRolePermissionService {

    /**
     * 根据资源URL查询角色列表
     * @param requestUrl
     * @return
     */
    List<Role> findRoleListByPermissionUrl(String requestUrl);
}
