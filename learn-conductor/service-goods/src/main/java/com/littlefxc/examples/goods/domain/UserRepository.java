package com.littlefxc.examples.goods.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author fengxuechao
 * @date 2019/1/3
 **/
@RepositoryRestResource(path="users")
public interface UserRepository extends JpaRepository<User, Long> {
}
