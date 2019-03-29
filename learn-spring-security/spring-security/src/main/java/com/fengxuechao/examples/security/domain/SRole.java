package com.fengxuechao.examples.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;  
import lombok.NoArgsConstructor;  
  
/** 
 * 角色信息 
 * @author fengxuechao
 * @date 2019-03-29
 */  
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class SRole {  
    private int id;  
    private String role;  
    private String describe;  
}  