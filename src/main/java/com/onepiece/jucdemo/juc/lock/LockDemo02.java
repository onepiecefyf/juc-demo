package com.onepiece.jucdemo.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对于某一个参数的版本，实现中断和虚假唤醒是可能的，而且此方法始终在循环中使用
 *
 * <p>线程通信 实现对某个数的加减
 *
 * <p>1、在资源类操作的方法 （1） 判断 （2） 干活 （3） 通知
 *
 * <p>2、创建多线程操作资源类
 *
 * @author fengyafei
 */
public class LockDemo02 {

  public static void main(String[] args) {

    Share share = new Share();

    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                try {
                  share.incr();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            "incr")
        .start();

    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                try {
                  share.decr();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            "decr")
        .start();

    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                try {
                  share.incr();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            "decr")
        .start();

    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                try {
                  share.decr();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            "decr")
        .start();
  }
}

class Share {

  Lock lock = new ReentrantLock();
  Condition condition = lock.newCondition();
  private int number = 1;

  public void incr() throws InterruptedException {
    lock.lock();
    try {
      if (number != 0) {
        condition.await();
      }
      number++;
      System.out.println(Thread.currentThread().getName() + " number: " + number);
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void decr() throws InterruptedException {
    lock.lock();
    try {
      if (number != 1) {
        condition.await();
      }
      number--;
      System.out.println(Thread.currentThread().getName() + " number: " + number);
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }
}
