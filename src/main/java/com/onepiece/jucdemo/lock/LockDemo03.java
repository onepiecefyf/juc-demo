package com.onepiece.jucdemo.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程间定制通信问题
 *
 * <p>案例 按照以下顺序执行 线程AA：向前走1步 线程BB：向前走2步 线程CC：向前走3步
 *
 * <p>AA -> BB -> CC 为一轮 一共执行3轮 使用线程定制通信实现
 *
 * <p>解决方案
 *
 * <p>1、添加资源类、资源类属性以及方法 2、为资源类添加标识位，区别线程
 *
 * <p>flag == 1 执行线程AA,修改flag == 2、并通知BB flag == 2 执行线程BB,修改flag == 3、并通知CC flag == 3 执行线程CC,修改flag
 * == 1、并通知AA
 *
 * <p>依次类推
 *
 * @author fengyafei
 */
public class LockDemo03 {

  public static void main(String[] args) {
    ShareResource resource = new ShareResource();

      new Thread(() ->{
        try {
          resource.print1(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }, "AA").start();


    new Thread(() ->{
      try {
        resource.print2(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "BB").start();


    new Thread(() ->{
      try {
        resource.print3(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "CC").start();

  }
}

class ShareResource {

  int flag = 1;
  Lock lock = new ReentrantLock();

  Condition condition1 = lock.newCondition();
  Condition condition2 = lock.newCondition();
  Condition condition3 = lock.newCondition();


  public void print1(int loop) throws InterruptedException{
    lock.lock();
    try {

      while (flag != 1) {
        condition1.await();
      }

      for (int i = 0; i < loop; i++) {
        System.out.println(Thread.currentThread().getName());
      }

      flag = 2;
      condition2.signal();

    } finally {
      lock.unlock();
    }
  }


  public void print2(int loop) throws InterruptedException{
    lock.lock();
    try {

      while (flag != 2) {
        condition1.await();
      }

      for (int i = 0; i < loop; i++) {
        System.out.println(Thread.currentThread().getName());
      }

      flag = 3;
      condition3.signal();

    } finally {
      lock.unlock();
    }
  }


  public void print3(int loop) throws InterruptedException{
    lock.lock();
    try {

      while (flag != 3) {
        condition1.await();
      }

      for (int i = 0; i < loop; i++) {
        System.out.println(Thread.currentThread().getName());
      }

      flag = 1;
      condition1.signal();

    } finally {
      lock.unlock();
    }
  }


}
