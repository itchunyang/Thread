package com.itchunyang.sync;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luchunyang on 2016/11/2.
 * Lock:对 synchronized 的改进
 * synchronized有一些功能性的限制:它无法中断一个正在等候获得锁的线程,也无法通过投票得到锁.
 * Lock : 等待可中断tryLock,公平锁。synchronized的是非公平锁,绑定多个Condition.await(),signal();
 *
 * public interface Lock {
 *     void lock();
 *     void lockInterruptibly() throws InterruptedException;
 *     boolean tryLock();
 *     boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
 *     void unlock();
 *     Condition newCondition();
 * }
 *
 * public interface Condition {
 *     void await() throws InterruptedException;
 *     void awaitUninterruptibly();
 *     long awaitNanos(long nanosTimeout) throws InterruptedException;
 *     boolean await(long time, TimeUnit unit) throws InterruptedException;
 *     boolean awaitUntil(Date deadline) throws InterruptedException;
 *     void signal();
 *     void signalAll();
 * }
 */

public class LockDemo {

    static final ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) {
        //使用公平锁的程序在许多线程访问时表现为很低的总体吞吐量（即速度很慢，常常极其慢）
//        ReentrantReadWriteLock

//        TickSell tickSell = new TickSell();
//        new Thread(tickSell).start();
//        new Thread(tickSell).start();
//        new Thread(tickSell).start();

        testInterrupt();
    }

    private static void testInterrupt() {
        new Thread(){
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        }.start();


        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
//                    lock.lockInterruptibly();//可以被打断
                    lock.lock();//不能被打断
                    System.out.println("获取到了锁");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println("lock.lockInterruptibly 被打断"+e.toString());
                }

                lock.unlock();
                System.out.println("释放锁");
            }
        };
        thread.start();

        try {
            Thread.sleep(2000);
            System.out.println("打断lock");
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static class TickSell implements Runnable{

        private int tick = 100;

        @Override
        public void run() {
            while( true){
                    lock.lock();//不响应Thread.interrupt()中断
                    try{
                        if(tick > 0 ){
                            System.out.println(Thread.currentThread().getName() + " sail -> " + (tick--));
                            Thread.sleep(100);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }

                }
            }
        }
}
