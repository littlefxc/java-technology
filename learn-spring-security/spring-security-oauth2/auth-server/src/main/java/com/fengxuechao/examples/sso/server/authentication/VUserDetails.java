package com.fengxuechao.examples.sso.server.authentication;

import com.fengxuechao.examples.sso.server.domain.SPermission;
import com.fengxuechao.examples.sso.server.domain.SRole;
import com.fengxuechao.examples.sso.server.domain.SUser;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义 UserDetailsService
 * 用户信息的封装，包含用户名称密码及用户状态、权限等信息 
 * @author Veiking 
 */
@Slf4j
@ToString
public class VUserDetails extends SUser implements UserDetails {
  
    private static final long serialVersionUID = 1L;

    //用户角色列表
    private List<SRole> sRoleList = null;

    //用户资源权限列表
    private List<SPermission> sPermissionList = null;

    /**
     * 注意后边的这两个参数：sRoleList、sPermissionList 
     * @param sUser 
     * @param sRoleList 
     * @param sPermissionList 
     */  
    public VUserDetails(SUser sUser, List<SRole> sRoleList, List<SPermission> sPermissionList) {
        super(sUser);  
        this.sRoleList = sRoleList;  
        this.sPermissionList = sPermissionList;  
    }  

    /**
     * 获取用户权限列表方法 
     * 可以理解成，返回了一个List<String>，之后所谓的权限控制、鉴权，其实就是跟这个list里的String进行对比 
     * 这里处理了角色和资源权限两个列表，可以这么理解， 
     * 角色是权限的抽象集合，是为了更方便的控制和分配权限，而真正颗粒化细节方面，还是需要资源权限自己来做 
     */  
    @Override  
    public Collection<? extends GrantedAuthority> getAuthorities() {
        StringBuilder authoritiesBuilder = new StringBuilder("");  
        List<SRole> tempRoleList = this.getsRoleList();  
        if (null != tempRoleList) {  
            for (SRole role : tempRoleList) {  
                authoritiesBuilder.append(",").append(role.getRole());  
            }  
        }  
        List<SPermission> tempPermissionList = this.getsPermissionList();  
        if (null != tempPermissionList) {  
            for (SPermission permission : tempPermissionList) {  
                authoritiesBuilder.append(",").append(permission.getPermission());  
            }  
        }  
        String authoritiesStr = "";  
        if(authoritiesBuilder.length()>0) {  
            authoritiesStr = authoritiesBuilder.deleteCharAt(0).toString();  
        }  
        log.info("VUserDetails getAuthorities [authoritiesStr={} ", authoritiesStr);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesStr);
    }  
  
    @Override  
    public String getPassword() {  
        return super.getPassword();  
    }  
  
    @Override  
    public String getUsername() {  
        return super.getName();  
    }  
  
    /** 
     * 判断账号是否已经过期，默认没有过期 
     */  
    @Override  
    public boolean isAccountNonExpired() {
        return true;  
    }  
  
    /** 
     * 判断账号是否被锁定，默认没有锁定 
     */  
    @Override  
    public boolean isAccountNonLocked() {  
        return true;  
    }  
  
    /** 
     * 判断信用凭证是否过期，默认没有过期 
     */  
    @Override  
    public boolean isCredentialsNonExpired() {  
        return true;  
    }  
  
    /** 
     * 判断账号是否可用，默认可用 
     */  
    @Override  
    public boolean isEnabled() {  
        return true;  
    }  
  
    public List<SRole> getsRoleList() {  
        return sRoleList;  
    }  
  
    public void setsRoleList(List<SRole> sRoleList) {  
        this.sRoleList = sRoleList;  
    }  
  
    public List<SPermission> getsPermissionList() {  
        return sPermissionList;  
    }  
  
    public void setsPermissionList(List<SPermission> sPermissionList) {  
        this.sPermissionList = sPermissionList;  
    }  
  
}  