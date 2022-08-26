package com.onepiece.jucdemo.service;

import com.onepiece.jucdemo.remote.BaseRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 远程调用
 *
 * @author fengyafei
 */
@Service
public class RemoteClientService {

  private static String url = "http://192.168.136.88:10600/keyTemplate/list";
  private BaseRemoteService baseRemoteService;

  @Autowired
  public void setBaseRemoteService(BaseRemoteService baseRemoteService) {
    this.baseRemoteService = baseRemoteService;
  }

  public ResponseEntity getDbSourceDetail() {
    return baseRemoteService.remoteGetEntity(url);
  }
}
