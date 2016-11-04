package com.itchunyang.sync;

/**
 * Created by luchunyang on 2016/11/2.
 */
public class WaitThreadDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new MyThread().start();

        Thread.sleep(1000);

        System.out.println("Main 准备获取锁");
        synchronized (lock){//阻塞！
            System.out.println("Main 获取到了锁,开始唤醒");
            lock.notifyAll();//也必须拥有对象锁
            Thread.sleep(8000);
            System.out.println("Main 释放锁");
        }

        Thread.sleep(3000);
        System.out.println("end");
    }

    static class MyThread extends Thread{

        @Override
        public void run() {
            synchronized (lock){
                System.out.println("线程获取到了锁");

                try {
                    Thread.sleep(5000);
                    //wait必须获取锁的前提，wait后会释放锁，wait后还是要抢锁的
                    lock.wait();
//                    lock.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程被唤醒了");
            }
            super.run();
        }
    }
}
