package com.onepiece.jucdemo;

import com.alibaba.fastjson.JSON;
import com.onepiece.jucdemo.service.RemoteClientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Slf4j
public class RemoteTest extends BaseTest {
  private RemoteClientService remoteClientService;

  @Autowired
  public void setRemoteClientService(RemoteClientService remoteClientService) {
    this.remoteClientService = remoteClientService;
  }

  @Test
  public void testRemote() {
    ResponseEntity dbSourceDetail = remoteClientService.getDbSourceDetail();
    System.out.println(JSON.toJSONString(dbSourceDetail));
  }



}
