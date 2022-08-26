package com.onepiece.jucdemo.remote;

import com.alibaba.druid.support.json.JSONUtils;
import com.onepiece.jucdemo.exception.RemotServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 远程调用
 *
 * @author yafei.feng
 */
@Slf4j
@Service
public class BaseRemoteService {

  private RestTemplate restTemplate;

  @Autowired
  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * 无参数 TOKEN POST请求
   *
   * @param url
   * @return
   */
  public <T> ResponseEntity<T> postAuthToken(String url) {
    log.info("远程调用入参:{}", url);
    HttpHeaders httpHeaders = this.buildTokenHeader();
    HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<T> responseEntity =
        restTemplate.exchange(
            url,
            HttpMethod.POST,
            httpEntity,
            new ParameterizedTypeReference<T>() {});
    log.info("远程调用响应:{}", JSONUtils.toJSONString(responseEntity));
    if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
      return responseEntity;
    }
    throw new RemotServiceException(responseEntity.getStatusCodeValue(), responseEntity.toString());
  }

  /**
   * 有参数 TOKEN GET请求
   *
   * @param url
   * @return
   */
  public <T> ResponseEntity<T> remoteGetEntity(String url) {
    log.info("远程调用入参、路径:{}", url);
    HttpHeaders httpHeaders = this.buildTokenHeader();
    HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
    String pageUrl = uriComponentsBuilder.build().toUriString();
    ResponseEntity<T> responseEntity =
        restTemplate.exchange(
            pageUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<T>() {});
    log.info("远程调用响应:{}", JSONUtils.toJSONString(responseEntity));
    if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
      return responseEntity;
    }
    throw new RemotServiceException(responseEntity.getStatusCodeValue(), responseEntity.toString());
  }

  private HttpHeaders buildBaseHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(
        "Authorization",
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NTkzNzU2NzksInVzZXJfbmFtZSI6Inlzc3MiLCJhdXRob3JpdGllcyI6WyIyIl0sImp0aSI6IjBlYjA0YzA2LWRhZjAtNGYyYS1hZDc0LTNiNGMzMzA2YTc3MCIsImNsaWVudF9pZCI6ImNsaWVudCIsInNjb3BlIjpbImFsbCJdfQ.L8JrBBvKlFjdiFFXBNIqNMu8b4_uipjUxqZPmpC1hqc");
    return headers;
  }

  private HttpHeaders buildTokenHeader() {
    HttpHeaders httpHeaders = this.buildBaseHeader();
    httpHeaders.setBearerAuth(
        "eyJhbGciOiJIUzUxMiJ9.eyJhZG1pblNhZmVWbyI6eyJpZCI6MjgwMjc1ODgyOTI3NDY0NDQ4LCJhY2NvdW50Ijoib3BlcmF0b3IiLCJuYW1lIjoieXN4Iiwic3RhdHVzIjoiTk9STUFMIiwicm9sZXMiOiJST0xFX0JJWl9PUEVSQVRPUiJ9LCJzdWIiOiJvcGVyYXRvciIsImV4cCI6MTY2MzYyNjIxNSwiaWF0IjoxNjYwMDI2MjE1LCJwZXJtaXNzb25zIjpbIlJPTEVfQklaX09QRVJBVE9SIl19.mhfhGNOFwxU6K02PrfqkC5UVeO9-w9wdo7n_rDUguTRjG7Iz0Ov0zXjz5y9w7AQtXoNoUIRXqEbiQ_0Gvo9smw");
    return httpHeaders;
  }
}
