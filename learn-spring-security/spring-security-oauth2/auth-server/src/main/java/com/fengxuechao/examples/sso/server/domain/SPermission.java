package com.fengxuechao.examples.sso.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;  
import lombok.NoArgsConstructor;  
/** 
 * 访问资源信息 
 * @author Veiking 
 */  
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class SPermission {  
    private int id;  
    private String permission;  
    private String url;  
    private String describe;  
}  