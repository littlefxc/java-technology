package com.fengxuechao.examples.rwdb.config;

import org.springframework.util.Assert;

/**
 * @author fengxuechao
 */
public class CustomerContextHolder {

   private static final ThreadLocal<CustomerType> contextHolder = new ThreadLocal<>();
	
   public static void setCustomerType(CustomerType customerType) {
      Assert.notNull(customerType, "customerType cannot be null");
      contextHolder.set(customerType);
   }

   public static CustomerType getCustomerType() {
      return contextHolder.get();
   }

   public static void clearCustomerType() {
      contextHolder.remove();
   }
}