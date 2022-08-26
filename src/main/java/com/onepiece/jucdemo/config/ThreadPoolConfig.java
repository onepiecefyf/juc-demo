package com.onepiece.jucdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * @author fengyafei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.threadpool")
public class ThreadPoolConfig {

  private int threadPoolCoreSize;
  private int threadPoolKeepAliveSeconds;
  private int threadPoolMaxSize;
  private int threadPoolQueueCapacity;


  @Bean(name = "taskExecutor")
  public ThreadPoolTaskExecutor getThreadPoolExecutor () {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    /** 线程池维护的最少线程数量 */
    taskExecutor.setCorePoolSize(threadPoolCoreSize);
    /** 线程池维护的线程最大数量 */
    taskExecutor.setMaxPoolSize(threadPoolMaxSize);
    /** 线程池维护的线程队列 */
    taskExecutor.setQueueCapacity(threadPoolQueueCapacity);
    /** 允许线程空闲时间 */
    taskExecutor.setKeepAliveSeconds(threadPoolKeepAliveSeconds);
    return taskExecutor;
  }

}
