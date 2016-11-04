package com.itchunyang.sync;

/**
 * Created by luchunyang on 2016/11/2.
 */
public class JoinThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread over.");
            }
        });
        thread.start();

        System.out.println("join开始");
        /**
         * thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程
         * 比如在线程B中调用了线程A的Join()方法，直到线程A执行完毕后，才会继续执行线程B。
         */
//        thread.join();
        thread.join(2000);
        System.out.println("join结束");
    }
}
