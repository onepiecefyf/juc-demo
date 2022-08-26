package com.onepiece.jucdemo.entity;

import lombok.Data;

/**
 * 响应参数封装
 * @author fengyafei
 */
@Data
public class ResultMessage {
  private int status;
  private String message;
  private String data;
}
