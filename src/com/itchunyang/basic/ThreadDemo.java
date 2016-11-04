package com.itchunyang.basic;

/**
 * Created by luchunyang on 2016/11/2.
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
//        testDaemon();
        testThreadGroup();
    }

    private static void testThreadGroup() throws InterruptedException {
        ThreadGroup group = new ThreadGroup("group-custom");
        /**
         * setDaemon 是设置ThreadGroup本身的，不是设置里面每个Thread的。
         * A daemon thread group is automatically destroyed when its last
         * thread is stopped or its last thread group is destroyed
         */
        group.setDaemon(true);
        group.setMaxPriority(3);//设置当前线程组允许的最大优先级

        Thread t1 = new Thread(group,new MyRunnable());
        t1.setPriority(8);//无效，受线程组限制，最大可设置3
        t1.start();

        System.out.println("返回此线程组中活动线程的估计数 = "+group.activeCount());
        System.out.println("thread Priority = "+t1.getPriority());
        System.out.println("thread group name = "+t1.getThreadGroup().getName());

//        Thread.sleep(1000);
//        group.interrupt();


        Thread.sleep(9000);
        //如果ThreadGroup 前面设置成了daemon，那么在这里会报异常，因为daemon ThreadGroup已经销毁了
        t1 = new Thread(group,new MyRunnable());
        t1.start();

    }

    private static void testDaemon() {
        Thread t = new Thread(new MyRunnable());
        // 如果jvm中都是后台进程，当前jvm将exit
        t.setDaemon(true);
        t.start();
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("休眠完毕");
        }
    }
}
