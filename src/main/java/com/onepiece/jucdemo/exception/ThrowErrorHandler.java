package com.onepiece.jucdemo.exception;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import com.alibaba.fastjson.JSON;
import com.onepiece.jucdemo.entity.ResultMessage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 远程连接异常处理
 *
 * @author fengyafei
 */
@Slf4j
@Component
public class ThrowErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    /** 客户端异常或者服务器异常 */
    return clientHttpResponse.getStatusCode().series() == CLIENT_ERROR
        || clientHttpResponse.getStatusCode().series() == SERVER_ERROR;
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    String response = String.valueOf(FileCopyUtils.copyToByteArray(clientHttpResponse.getBody()));
    log.error("远程连接异常信息: {}", response);
    ResultMessage resultMessage = JSON.parseObject(response, ResultMessage.class);
    throw new RemotServiceException(
        resultMessage.getStatus(), resultMessage.getMessage(), resultMessage.getData());
  }
}
