package com.itchunyang.pool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Java里面线程池的顶级接口是Executor
 * public interface Executor {
 * 		void execute(Runnable command);
 * }
 *
 *
 * public interface ExecutorService extends Executor{
 * 		void shutdown();
 * 		List<Runnable> shutdownNow();
 * 		boolean isShutdown();
 * 		boolean isTerminated();
 * 		<T> Future<T> submit(Runnable task, T result);
 * 		<T> Future<T> submit(Callable<T> task);
 * 		Future<?> submit(Runnable task);
 * 		...
 * }
 *
 * Executors该类是一个辅助类
 * public class Executors {
 * 		newFixedThreadPool(int nThreads);
 * 		newFixedThreadPool(int nThreads, ThreadFactory threadFactory);
 * 		newSingleThreadExecutor();
 * 		newSingleThreadExecutor(ThreadFactory threadFactory);
 * 		newCachedThreadPool();
 * 		newCachedThreadPool(ThreadFactory threadFactory);
 * 		newSingleThreadScheduledExecutor();
 * 		newSingleThreadScheduledExecutor(ThreadFactory threadFactory);
 * 		newScheduledThreadPool(int corePoolSize);
 * 		newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory);
 * }
 *
 */


public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        singleThreadPool();
//        cachedThreadPool();
//        fixedThreadPool();
//        scheduledThreadPool();
    }

    private static void scheduledThreadPool() {
        //不关闭，不会退出
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
        MyThread t1 = new MyThread("线程1",2000);
        MyThread t2 = new MyThread("线程2",2000);
        MyThread t3 = new MyThread("线程3",2000);
        MyThread t4 = new MyThread("线程4",2000);
        MyThread t5 = new MyThread("线程5",2000);

//        pool.execute(t1);
//		pool.execute(t2);
//		pool.execute(t3);
//		pool.execute(t4);
//		pool.execute(t5);

//        pool.schedule(t1,6000, TimeUnit.MILLISECONDS);

        //延迟2秒后，每隔3秒执行一次。并且保证在上次运行完后才会执行下一次。
        //即无论某个任务执行多长时间，等执行完了，我再延迟指定的时间
//        pool.scheduleWithFixedDelay(t2,2,3,TimeUnit.SECONDS);

        //已固定的频率来执行某项计划  它不受计划执行时间的影响。到时间，它就执行。
//		pool.scheduleAtFixedRate(t4, 2, 3, TimeUnit.SECONDS);

        //每天凌晨1点执行一次
        long oneDay = 24 * 60 * 60 * 1000;//时间间隔(一天)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10); //凌晨1点
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime(); //第一次执行定时任务的时间

        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            System.out.println("小于当前时间了");
            date = addDay(date, 1);
        }
        pool.scheduleAtFixedRate(t1, date.getTime() - System.currentTimeMillis(), oneDay,TimeUnit.MILLISECONDS);

    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    private static void fixedThreadPool() {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        //如果编写的是小程序，或者是轻载的服务器，使用Excutors.newCashedThreadpool通常是个不错的选择，
        //因为它不需要配置，并且一般情况下都能够正确地完成工作。但是对于大负载的服务器来说，
        //缓存的线程池就不是很好的选择了！在缓存的线程池中，被提交的任务没有排成队列。
        //而是直接交给线程执行。如果没有线程可用，就创建一个新的线程。如果服务器负载的太重，以致他所有的CPU都完全被占用了，
        //当有更多的任务时，就会创建更多的线程，这样只会使情况变得更糟。因此在大负载的产品服务器中，
        //最好使用Excutors.newFixedThreadPool，它为你提供了一个包含固定线程数目的线程池，
        //或者为了最大的限度地控制它，就直接使用ThreadPoolExcutor类。

        //创建固定大小的线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        MyThread t1 = new MyThread("线程1");
        MyThread t2 = new MyThread("线程2");
        MyThread t3 = new MyThread("线程3");
        MyThread t4 = new MyThread("线程4");
        MyThread t5 = new MyThread("线程5");

        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t1);
    }

    private static void cachedThreadPool() {
        //创建一个可缓存的线程池。会回收部分空闲（60秒不执行任务）的线程
        //当任务数增加时，先查看池中有没有以前建立的可用的空闲线程。如果没有则添加新线程来处理任务
        //此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统
        ExecutorService pool = Executors.newCachedThreadPool();

        MyThread t1 = new MyThread("线程1");
        MyThread t2 = new MyThread("线程2");
        MyThread t3 = new MyThread("线程3");
        MyThread t4 = new MyThread("线程4");
        MyThread t5 = new MyThread("线程5");

        pool.execute(t1);
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);

        //60s后，线程将结束程序退出!!!

    }

    private static void singleThreadPool() throws InterruptedException {
        //创建一个单线程的线程池，此线程池保证所有任务的执行顺序按照任务的提交顺序执行
        ExecutorService pool = Executors.newSingleThreadExecutor();

        MyThread t1 = new MyThread("线程1");
        MyThread t2 = new MyThread("线程2");
        MyThread t3 = new MyThread("线程3");
        MyThread t4 = new MyThread("线程4");

        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);

        /**
         * 当线程池调用该方法时,线程池的状态则立刻变成SHUTDOWN状态,以后不能再往线程池中添加任何任务，
         * 否则将会抛出RejectedExecutionException异常。但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。
         */
//        pool.shutdown();
//        pool.execute(t4);

//        Thread.sleep(1000);
        //执行后不再接受新任务，如果有等待任务，移出队列；有正在执行的，尝试停止之(它通过调用Thread.interrupt来实现线程的立即退出)
        //返回一个正在等待执行但线程池关闭却没有被执行的Task集合
//        List<Runnable> runnables = pool.shutdownNow();
//        for(int i=0;i<runnables.size();i++){
//            MyThread runnable = (MyThread) runnables.get(i);
//            System.out.println("未执行的线程:"+runnable.getName());
//        }

    }



    static class MyThread extends Thread{
        private int sleep;
        public MyThread(String name, int sleep) {
            super(name);

            this.sleep = sleep;
        }


        public MyThread(String name) {
            this(name,3000);
        }

        @Override
        public void run() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(new Date())+" "+"("+getName()+")"+"正在执行...");
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(df.format(new Date()) +" ("+getName()+")"+"执行完毕\n");
        }
    }
}
