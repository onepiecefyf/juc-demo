package com.onepiece.jucdemo.config;

import com.onepiece.jucdemo.exception.RemotServiceException;
import com.onepiece.jucdemo.exception.ThrowErrorHandler;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 远程调用配置
 *
 * @author fengyafei
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rest.connection")
@Data
@Slf4j
public class RestTemplateConfig {

  /** 连接服务器的（握手成功）时间 */
  private int connectTimeout;
  /** 服务器返回（响应）的时间 */
  private int readTimeout;
  /** 允许连接的最大线程数 */
  private int maxTotal;
  /** 默认线程数 */
  private int maxPerRoute;
  /** 从连接池中获取连接超时时间 */
  private int connectionRequestTimeout;

  @Bean
  public RestTemplate getRestTemplate() throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(clientHttpRequestFactory());
    restTemplate.setErrorHandler(new ThrowErrorHandler());
    return restTemplate;
  }

  @Bean
  public ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    SSLContext sslContext =
        new SSLContextBuilder()
            .loadTrustMaterial(null, (TrustStrategy) (arg0, arg1) -> true)
            .build();
    httpClientBuilder.setSSLContext(sslContext);
    HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
    SSLConnectionSocketFactory sslConnectionSocketFactory =
        new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create()
            // 注册http和https请求
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslConnectionSocketFactory)
            .build();
    // 开始设置连接池
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
        new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    // 最大连接数2700
    poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
    // 同路由并发数100
    poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
    httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
    // 重试次数
    httpClientBuilder.setRetryHandler(
        (exception, executionCount, context) -> {
          log.info("远程连接尝试次数：{}", executionCount);
          if (executionCount > 3) {
            log.warn("Maximum tries reached for client http pool ");
            throw new RemotServiceException(exception);
          }

          if (exception instanceof NoHttpResponseException // NoHttpResponseException 重试
              || exception instanceof ConnectTimeoutException // 连接超时重试
              || exception instanceof SocketException // 连接超时重试
              || exception instanceof SocketTimeoutException) {
            log.warn("NoHttpResponseException on " + executionCount + " call");
            return true;
          }
          return false;
        });
    httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
    httpClientBuilder.disableCookieManagement();
    /**
     * ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
     * if(keepAliveStrategyCopy == null) {
     * keepAliveStrategyCopy=DefaultConnectionKeepAliveStrategy.INSTANCE; }
     * /org/apache/http/impl/client/HttpClientBuilder.java:1019
     */
    HttpClient httpClient = httpClientBuilder.build();
    // httpClient连接配置
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
    // 连接超时
    clientHttpRequestFactory.setConnectTimeout(connectTimeout);
    // 数据读取超时时间
    clientHttpRequestFactory.setReadTimeout(readTimeout);
    // 连接不够用的等待时间
    clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
    return clientHttpRequestFactory;
  }
}
