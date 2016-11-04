package com.itchunyang.sync;

/**
 * Created by luchunyang on 2016/11/2.
 * <p>
 * Thread.interrupt()方法
 * 会使正在运行的线程中断状态(interrupted status) 被置位
 * <p>
 * 如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。
 * 抛出InterruptedException异常后，中断标示位会自动清除
 */
public class InterruptThreadDemo {

    public static void main(String[] args) throws InterruptedException {
//        testSleep();
    }

    private static void testSleep() throws InterruptedException {
        Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("I am running");

                        // 休眠一断时间,中断时会抛出InterruptedException 调用sleep的时候锁并没有被释放。
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    //如果触发中断异常了，中断状态被清除，所以下面检测isInterrupted() 一直为false
                    //因为抛出InterruptedException异常后，中断标示位会自动清除.Thread.currentThread().interrupt();这句可以来设置中断状态。
                    System.out.println("isInterrupted:" + isInterrupted());//false
                    e.printStackTrace();
                }
            }
        };
        sleepThread.start();

        Thread.sleep(8000);
        System.out.println("打断sleepThread");
        sleepThread.interrupt();
    }
}
