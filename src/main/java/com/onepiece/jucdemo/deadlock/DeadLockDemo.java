package com.onepiece.jucdemo.deadlock;

import java.util.concurrent.TimeUnit;

/**
 * 死锁产生的案例
 *
 * <p>1、创建两个资源 2、创建两个线程，互相竞争两个资源
 *
 * @author fengyafei
 */
public class DeadLockDemo {
  public static void main(String[] args) {
    Object a = new Object();
    Object b = new Object();

    new Thread(
            () -> {
              synchronized (a) {
                System.out.println(Thread.currentThread().getName() + " 获得资源a");
              }

              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              synchronized (b) {
                System.out.println(Thread.currentThread().getName() + " 获得资源b");
              }
            },
            "A")
        .start();

    new Thread(
            () -> {
              synchronized (b) {
                System.out.println(Thread.currentThread().getName() + " 获得资源b");
              }

              synchronized (a) {
                System.out.println(Thread.currentThread().getName() + " 获得资源a");
              }
            },
            "B")
        .start();
  }
}
