package com.itchunyang.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore实现的功能就类似厕所有5个坑，假如有10个人要上厕所，那么同时只能有多少个人去上厕所呢？同时只能有5个人能够占用，当5个人中 的任何一个人让开后，
 * 其中等待的另外5个人中又有一个人可以占用了。另外等待的5个人中可以是随机获得优先机会，也可以是按照先来后到的顺序获得机会，
 * 这取决于构造Semaphore对象时传入的参数选项。单个信号量的Semaphore对象可以实现互斥锁的功能，并且可以是由一个线程获得了“锁”，再由另一个线程释放“锁”，这可应用于死锁恢复的一些场合。
 *
 */
public class SemaphoreDemo {
    final static Semaphore semaphore = new Semaphore(5,false);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        //只能五个线程同时使用

        for (int i = 0; i < 20; i++) {
            MyRunnable runnable = new MyRunnable(i);
            pool.execute(runnable);
        }

        pool.shutdown();
    }

    static class MyRunnable implements Runnable{

        int n;

        public MyRunnable(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            try {
                System.out.println("正在获取许可 "+n);
                semaphore.acquire();
                System.out.println("获取了许可 " + n);
                Thread.sleep((long) (Math.random() * 10000));
                //释放资源
                semaphore.release();
                //availablePermits()指的是当前信号灯库中有多少个可以被使用
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
