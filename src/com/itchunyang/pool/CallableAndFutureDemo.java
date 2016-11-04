package com.itchunyang.pool;

import java.util.concurrent.*;

/**
 * Created by luchunyang on 2016/11/4.
 *
 * Callable接口类似于Runnable,Callable功能更强大一些，被线程执行后，可以返回值，这个返回值可以被Future拿到
 * 1.Callable 使用 call（） 方法， Runnable 使用 run() 方法
 * 2.call() 可以返回值， 而 run()方法不能返回。
 * 3.call() 可以抛出受检查的异常，而run()不能抛出受检查的异常
 *
 * public interface Callable<V> {
 * 		V call() throws Exception;
 * }
 *
 * 那么怎么使用Callable呢？
 * Callable 两种使用方式:
 * 		1)Callable交给线程池运行，会返回一个Future对象.
 * 		2)用Futrue包装一下，然后把Future交给Thread运行
 *
 *
 *
 * Future
 * Future就是对于具体的Runnable或者Callable任务的执行结果进行取消、查询是否完成、获取结果
 * FutureTask FutureTask是Future接口的一个唯一实现类.它继成了Runnable和Future接口,
 * 所以它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值
 *
 */
public class CallableAndFutureDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        test1();
//        test2();
//        test3();
        cancelTest();

    }

    private static void cancelTest()  {
        FutureTask<String> futureTask = new FutureTask<>(new MyCall());
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(futureTask);
//        new Thread(futureTask).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //如果为true，表示取消任务时，会发送中断！需要注意的是如果任务正常终止、异常或者取消都将返回true
        boolean ok = futureTask.cancel(true);
        System.out.println("取消结果 : " + ok);


    }

    //Callable + FutureTask
    private static void test3() throws ExecutionException, InterruptedException {

        FutureTask<String> futureTask = new FutureTask<>(new MyCall());
        //包装Runnable
//        FutureTask<String> futureTask = new FutureTask(new MyRunnable(),"is");


        //FutureTask 使用方式1
        new Thread(futureTask).start();

        //FutureTask 使用方式2
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(futureTask);

        System.out.println("result : "+futureTask.get());
    }

    private static void test2() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<String> future = pool.submit(new MyCall());

        try {
            //时间内获取不到结果会抛TimeoutException
            String result = future.get(6,TimeUnit.SECONDS);
//            String result = future.get(); //一直阻塞读取结果
            System.out.println("result : " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private static void test1() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();

//        Future future = pool.submit(new MyRunnable());

        Future<String> future = pool.submit(new MyCall());
        for (int i = 0; i < 7; i++) {
            boolean isDone = future.isDone();
            if(isDone){
                //future.get()只能拿到Callable的结果，拿Runnable的结果会是null
                System.out.println("Callable result : "+future.get());
                break;
            }else
                System.out.println("Callable isnot done ! ");

            Thread.sleep(1000);
        }
    }

    static class MyCall implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("MyCall开始执行");
            Thread.sleep(5000);
            System.out.println("MyCall执行完毕");
            return "MyCall is over";
        }
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println("MyRunnable开始执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MyRunnable执行完毕");
        }
    }
}
