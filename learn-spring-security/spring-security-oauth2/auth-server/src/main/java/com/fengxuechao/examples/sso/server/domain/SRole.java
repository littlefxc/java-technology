package com.fengxuechao.examples.sso.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;  
import lombok.NoArgsConstructor;  
  
/** 
 * 角色信息 
 * @author Veiking 
 */  
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class SRole {  
    private int id;  
    private String role;  
    private String describe;  
}  