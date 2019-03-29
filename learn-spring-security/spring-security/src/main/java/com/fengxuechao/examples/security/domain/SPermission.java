package com.fengxuechao.examples.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;  
import lombok.NoArgsConstructor;  
/** 
 * 访问资源信息 
 * @author fengxuechao
 * @date 2019-03-29
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