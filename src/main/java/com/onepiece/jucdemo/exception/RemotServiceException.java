package com.onepiece.jucdemo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 远程调用异常类
 *
 * @author meng ran
 * @date 2018-10-31 11:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RemotServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private String message;

  private String data;
  private int status;
  /** 错误代码 */

  public RemotServiceException(int status, String message, String data) {
    super(message);
    this.message = message;
    this.data = data;
    this.status = status;
  }

  public RemotServiceException(int status, String message) {
    super(message);
    this.message = message;
    this.status = status;
  }

  public RemotServiceException(Throwable cause) {
    super(cause);
  }


  public RemotServiceException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
