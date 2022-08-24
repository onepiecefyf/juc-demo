package com.onepiece.jucdemo.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 初始化三个线程（售票员）售票
 *
 * 创建资源类（票），初始化属性（票数）与方法（售票窗口）
 *
 * @author fengyafei
 */
public class LockDemo01 {

  public static void main(String[] args) {

    Ticket ticket = new Ticket();

    new Thread(new Runnable() {
      @Override
      public void run() {
        ticket.sale();
      }
    }, "小冯").start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        ticket.sale();
      }
    }, "小马").start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        ticket.sale();
      }
    }, "小王").start();



  }
}




class Ticket {

  Lock lock = new ReentrantLock();
  private int count = 30;

  public void sale() {
    lock.lock();
    try {
      while (count > 0) {
        System.out.println(Thread.currentThread().getName() + "卖出 " + count -- + "还剩下：" + count);
      }
    } finally{
      lock.unlock();
    }
  }

}
