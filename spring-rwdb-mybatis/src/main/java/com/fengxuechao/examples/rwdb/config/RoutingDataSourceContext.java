package com.fengxuechao.examples.rwdb.config;

import org.springframework.util.Assert;

/**
 * @author fengxuechao
 */
public class RoutingDataSourceContext implements AutoCloseable {

   private static final ThreadLocal<RoutingType> contextHolder = new ThreadLocal<>();

   public RoutingDataSourceContext(RoutingType routingType) {
      contextHolder.set(routingType);
   }
	
   public static void setRoutingType(RoutingType routingType) {
      Assert.notNull(routingType, "routingType cannot be null");
      contextHolder.set(routingType);
   }

   public static RoutingType getRoutingType() {
      return contextHolder.get();
   }

   public static void clear() {
      contextHolder.remove();
   }

   @Override
   public void close() throws Exception {
      clear();
   }
}