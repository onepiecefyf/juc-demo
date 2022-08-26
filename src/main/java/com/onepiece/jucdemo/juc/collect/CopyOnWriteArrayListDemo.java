package com.onepiece.jucdemo.juc.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 测试列表线程安全问题
 *
 * 1、Vector实现列表线程安全
 * 2、CollectionUtils.sy
 *
 *
 *
 * @author fengyafei
 */
public class CopyOnWriteArrayListDemo {

  public static void main(String[] args) {

    new Thread(() -> {
      addInVector();
    }).start();

    new Thread(() -> {
      addInVector();
    }).start();


  }

  private static void addInVector() {
    List list = new Vector();
    for (int i = 0; i < 1000000; i++) {
      list.add(i);
    }
  }

  private static void addInCopyOnWriteList() {
    List list = new CopyOnWriteArrayList();
    for (int i = 0; i < 1000000; i++) {
      list.add(i);
    }
  }

  private static void addInCollections() {
    List list = new ArrayList();
    List list1 = Collections.synchronizedList(list);
    for (int i = 0; i < 1000000; i++) {
      list1.add(i);
    }
  }




}


